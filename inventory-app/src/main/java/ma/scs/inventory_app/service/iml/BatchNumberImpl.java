package ma.scs.inventory_app.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.scs.inventory_app.dto.BatchNumberDTO;
import ma.scs.inventory_app.dto.BatchNumberImportResult;
import ma.scs.inventory_app.entities.BatchNumber;
import ma.scs.inventory_app.entities.PartNumber;
import ma.scs.inventory_app.mapper.BatchNumberMapper;
import ma.scs.inventory_app.repository.jpa.BatchNumberRepository;
import ma.scs.inventory_app.repository.jpa.PartNumberRepository;
import ma.scs.inventory_app.service.BatchNumberService;


import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;

// Lombok
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Apache POI (Excel)
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

// Java Standard Library
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchNumberImpl implements BatchNumberService {
    private final BatchNumberRepository batchNumberRepository;
    private final PartNumberRepository partNumberRepository; // Add this dependency
    
    @Override
    public Page<BatchNumber> getAllBatchNumbers(Pageable pageable) {
        return batchNumberRepository.findAll(pageable);
               
    }
    
    @Override
    public BatchNumber getDefaultBatchNumber() {
    	return batchNumberRepository.findFirstByIsDefaultBnTrue().orElse(null);
    	
    }

    @Override
    public BatchNumber getBatchById(Long id) {
        return batchNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));
    }

    @Override
    public BatchNumber getBatchByBn(String bn) {
        return batchNumberRepository.findByBn(bn)
                .orElseThrow(() -> new RuntimeException("Part not found with PN: " + bn));
    }

    @Override
    public BatchNumber saveBatch(BatchNumber batchNumber) {
        return batchNumberRepository.save(batchNumber);
    }
    
    @Override
    public BatchNumber saveDefaultBatch(BatchNumber batchNumber) {
    	batchNumber.setDefaultBn(true);
        return batchNumberRepository.save(batchNumber);
    }

    @Override
    public BatchNumber updateBatch(Long id, BatchNumber updatedBatch) {
        BatchNumber existing = getBatchById(id);
        updatedBatch.setId(existing.getId());
        return batchNumberRepository.save(updatedBatch);
    }

    @Override
    public void deleteBatch(Long id) {
        batchNumberRepository.deleteById(id);
    }
    
    
    @Override
    @Transactional
    public BatchNumberImportResult importBatchNumbersFromExcel(MultipartFile file) {
        BatchNumberImportResult result = new BatchNumberImportResult();
        
        log.info("Starting Excel import for file: {}", file.getOriginalFilename());
        
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = createWorkbook(file, inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            log.info("Sheet found with {} rows", sheet.getLastRowNum() + 1);
            
            // Validate Excel structure first
            if (!validateExcelStructure(sheet)) {
                throw new IllegalArgumentException("Excel file must have at least 2 columns: Batch Number, Part Number");
            }
            
            // Skip header row if exists (start from row 1)
            int startRow = hasHeaderRow(sheet) ? 1 : 0;
            log.info("Starting processing from row: {}", startRow);
            
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isEmptyRow(row)) {
                    log.debug("Skipping empty row: {}", i);
                    continue;
                }
                
                try {
                    BatchNumber batchNumber = parseRowToBatchNumber(row, i + 1);
                    if (batchNumber != null) {
                        log.info("Parsed BatchNumber: bn={}, partNumber={}", 
                               batchNumber.getBn(), 
                               batchNumber.getPartNumber() != null ? batchNumber.getPartNumber().getPn() : "null");
                        
                        BatchNumber savedBatchNumber = saveBatchNumber(batchNumber);
                        result.addSuccess(savedBatchNumber);
                        log.info("Successfully saved BatchNumber with ID: {}", savedBatchNumber.getId());
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", i + 1, e.getMessage());
                    result.addError(i + 1, e.getMessage());
                }
            }
            
            log.info("Import completed. Success: {}, Errors: {}", 
                   result.getSuccessCount(), result.getErrorCount());
            
        } catch (IOException e) {
            log.error("Error reading Excel file", e);
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }
        
        return result;
    }
    
    /**
     * Validate Excel structure
     */
    private boolean validateExcelStructure(Sheet sheet) {
        if (sheet.getLastRowNum() < 0) {
            return false;
        }
        
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            return false;
        }
        
        // Check if we have at least 2 columns
        return firstRow.getLastCellNum() >= 2;
    }
    
    /**
     * Check if row is empty
     */
    private boolean isEmptyRow(Row row) {
        if (row == null) return true;
        
        for (int i = 0; i < 2; i++) { // Check first 2 cells
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell, "").trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Parse Excel row to BatchNumber entity
     */
    private BatchNumber parseRowToBatchNumber(Row row, int rowNumber) {
        try {
            // Column 0: Batch Number (required)
            String batchNumberStr = getCellValueAsString(row.getCell(0), "");
            if (batchNumberStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Batch Number cannot be empty at row " + rowNumber);
            }
            
            log.debug("Processing batch number: {}", batchNumberStr.trim());
            
            // Check if batch number already exists
            if (batchNumberRepository.existsByBnIgnoreCase(batchNumberStr.trim())) {
                throw new IllegalArgumentException("Batch Number '" + batchNumberStr.trim() + "' already exists");
            }
            
            // Column 1: Part Number (optional)
            String partNumberStr = getCellValueAsString(row.getCell(1), "");
            
            BatchNumber batchNumber = new BatchNumber();
            batchNumber.setBn(batchNumberStr.trim());
             // Default value
            
            // Find associated PartNumber if provided
            if (!partNumberStr.trim().isEmpty()) {
                log.debug("Looking for part number: {}", partNumberStr.trim());
                Optional<PartNumber> partNumber = partNumberRepository.findByPn(partNumberStr.trim());
                if (partNumber.isPresent()) {
                    batchNumber.setPartNumber(partNumber.get());
                    log.debug("Found part number with ID: {}", partNumber.get().getId());
                } else {
                    // Part number doesn't exist, set FK to null
                    batchNumber.setPartNumber(null);
                    log.warn("Part Number '{}' not found for batch '{}'. FK set to null.", 
                           partNumberStr.trim(), batchNumberStr.trim());
                }
            } else {
                batchNumber.setPartNumber(null);
                log.debug("No part number provided for batch: {}", batchNumberStr.trim());
            }
            
            return batchNumber;
            
        } catch (Exception e) {
            log.error("Error parsing row {}: {}", rowNumber, e.getMessage());
            throw new IllegalArgumentException("Error parsing row " + rowNumber + ": " + e.getMessage());
        }
    }
    
    /**
     * Save BatchNumber with duplicate check
     */
    private BatchNumber saveBatchNumber(BatchNumber batchNumber) {
        try {
            log.debug("Attempting to save batch number: {}", batchNumber.getBn());
            BatchNumber saved = batchNumberRepository.save(batchNumber);
            log.debug("Successfully saved with ID: {}", saved.getId());
            return saved;
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation for batch: {}", batchNumber.getBn(), e);
            throw new IllegalArgumentException("Batch Number '" + batchNumber.getBn() + "' already exists in database");
        } catch (Exception e) {
            log.error("Unexpected error saving batch: {}", batchNumber.getBn(), e);
            throw new RuntimeException("Error saving batch number: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create appropriate workbook based on file extension
     */
    private Workbook createWorkbook(MultipartFile file, InputStream inputStream) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else {
            return new HSSFWorkbook(inputStream);
        }
    }
    
    /**
     * Check if first row contains headers
     */
    private boolean hasHeaderRow(Sheet sheet) {
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) return false;
        
        String firstCell = getCellValueAsString(firstRow.getCell(0), "");
        String secondCell = getCellValueAsString(firstRow.getCell(1), "");
        
        // Simple heuristic: if first row contains common header terms, skip it
        return (firstCell.toLowerCase().contains("batch") || 
                firstCell.toLowerCase().contains("bn") ||
                firstCell.toLowerCase().contains("number")) ||
               (secondCell.toLowerCase().contains("part") ||
                secondCell.toLowerCase().contains("pn") ||
                secondCell.toLowerCase().contains("number"));
    }
    
    /**
     * Helper method to get cell value as string with default value
     */
    private String getCellValueAsString(Cell cell, String defaultValue) {
        if (cell == null) {
            return defaultValue;
        }
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    // Handle numeric values that should be treated as strings
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        // Format numeric value to avoid scientific notation
                        double numericValue = cell.getNumericCellValue();
                        if (numericValue == (long) numericValue) {
                            return String.valueOf((long) numericValue);
                        } else {
                            return String.valueOf(numericValue);
                        }
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return defaultValue;
                default:
                    return defaultValue;
            }
        } catch (Exception e) {
            log.warn("Error reading cell value, using default: {}", e.getMessage());
            return defaultValue;
        }
    }
    
    
    
}



