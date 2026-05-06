package ma.scs.inventory_app.service.iml;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.PartNumberDTO;
import ma.scs.inventory_app.entities.PartNumber;
import ma.scs.inventory_app.mapper.PartNumberMapper;
import ma.scs.inventory_app.repository.jpa.PartNumberRepository;
import ma.scs.inventory_app.service.PartNumberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import  ma.scs.inventory_app.dto.UploadResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.function.Function;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;

    @Override
    public Page<PartNumberDTO> getAllParts(Pageable pageable) {
        return partNumberRepository.findAll(pageable)
                .map(PartNumberMapper::toDTO);
    }

    @Override
    public PartNumber getPartById(Long id) {
        return partNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));
    }

    @Override
    public PartNumber getPartByPn(String pn) {
        return partNumberRepository.findByPn(pn)
                .orElseThrow(() -> new RuntimeException("Part not found with PN: " + pn));
    }

    @Override
    public PartNumber savePart(PartNumber partNumber) {
        return partNumberRepository.save(partNumber);
    }

    @Override
    public PartNumber updatePart(Long id, PartNumber updatedPart) {
        PartNumber existing = getPartById(id);
        updatedPart.setId(existing.getId());
        return partNumberRepository.save(updatedPart);
    }

    @Override
    public void deletePart(Long id) {
        partNumberRepository.deleteById(id);
    }
    
    public UploadResult processExcelFile(MultipartFile file) throws IOException {
        // Default batch size of 2000 rows
        return processExcelFile(file, 2000);
    }

    public UploadResult processExcelFile(MultipartFile file, int totalRowsInSave) throws IOException {
        // Validate file
        validateExcelFile(file);
        
        // Parse Excel file to entities
        List<PartNumber> partNumbers = parseExcelToPartNumbers(file);
        
        // Process and save to database in batches
        return savePartNumbersInBatches(partNumbers, totalRowsInSave);
    }

    /**
     * Validate that the uploaded file is a valid Excel file
     */
    private void validateExcelFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            throw new IllegalArgumentException("File must be an Excel file (.xlsx or .xls)");
        }
        
        // Check file size (e.g., max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size too large. Maximum 10MB allowed.");
        }
    }

    /**
     * Parse Excel file to PartNumber entities
     */
    private List<PartNumber> parseExcelToPartNumbers(MultipartFile file) throws IOException {
        List<PartNumber> partNumbers = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            // Get first sheet (you can modify this to handle multiple sheets)
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (assuming row 0 is header)
            boolean isFirstRow = true;
            
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header row
                }
                
                // Skip empty rows
                if (isRowEmpty(row)) {
                    continue;
                }
                
                try {
                    PartNumber partNumber = parseRowToPartNumber(row);
                    if (partNumber != null) {
                        partNumbers.add(partNumber);
                    }
                } catch (Exception e) {
                    // Log error but continue processing other rows
                	System.out.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
                }
            }
        }
        
        return partNumbers;
    }

    /**
     * Parse a single row to PartNumber entity
     * Adjust column indices based on your Excel structure
     * Mapping to your DB: pn, pnDesc, price, totalStock, unit
     */
    private PartNumber parseRowToPartNumber(Row row) {
        try {
            PartNumber partNumber = new PartNumber();
            
            // Assuming Excel columns are: PN | Description | Price | Total Stock | Unit
            // Adjust these indices based on your actual Excel structure
            
            // Column 0: Part Number (required)
            String pn = getCellValueAsString(row.getCell(0));
            if (pn == null || pn.trim().isEmpty()) {
                throw new IllegalArgumentException("Part Number cannot be empty");
            }
            partNumber.setPn(pn.trim());
            
            // Column 1: Part Description
            String pnDesc = getCellValueAsString(row.getCell(1));
            partNumber.setPnDesc(pnDesc != null ? pnDesc.trim() : null);
            
            // Column 2: Price (Float)
            BigDecimal price = getCellValueAsBigDecimal(row.getCell(4));
            partNumber.setPrice(price.stripTrailingZeros());
            
            // Column 3: Total Stock (Float)
            BigDecimal totalStock = getCellValueAsBigDecimal(row.getCell(3));
            partNumber.setTotalStock(totalStock.stripTrailingZeros());
            
            // Column 4: Unit
            String unit = getCellValueAsString(row.getCell(2));
            partNumber.setUnit(unit != null ? unit.trim() : null);
            
            return partNumber;
            
        } catch (Exception e) {
           // log.warn("Error parsing row {}: {}", row.getRowNum(), e.getMessage());
            return null; // Skip invalid rows
        }
    }

    /**
     * Helper method to get cell values
     */
    

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    // Use BigDecimal.valueOf to avoid floating-point precision loss
                    return BigDecimal.valueOf(cell.getNumericCellValue());

                case STRING:
                    String stringValue = cell.getStringCellValue();
                    if (stringValue == null || stringValue.trim().isEmpty()) {
                        return null;
                    }
                    return new BigDecimal(stringValue.trim());

                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null; // Invalid number format
        }
    }

    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * Helper method to get cell value as Float
     */
    private Float getCellValueAsFloat(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (float) cell.getNumericCellValue();
                case STRING:
                    String stringValue = cell.getStringCellValue();
                    if (stringValue == null || stringValue.trim().isEmpty()) {
                        return null;
                    }
                    return Float.parseFloat(stringValue.trim());
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null; // Invalid number format
        }
    }
    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue(); // Already a double
                case STRING:
                    String stringValue = cell.getStringCellValue();
                    if (stringValue == null || stringValue.trim().isEmpty()) {
                        return null;
                    }
                    return Double.parseDouble(stringValue.trim());
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null; // Invalid number format
        }
    }


    /**
     * Check if row is empty
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Save part numbers to database in batches with configurable batch size
     */
    @Transactional
    private UploadResult savePartNumbersInBatches(List<PartNumber> partNumbers, int totalRowsInSave) {
        if (partNumbers.isEmpty()) {
            return new UploadResult(0, 0,0, 0, Collections.emptyList());
        }
        
        UploadResult finalResult = new UploadResult(0,0, 0, 0, new ArrayList<>());
        int batchNumber = 1;
        int totalBatches = (int) Math.ceil((double) partNumbers.size() / totalRowsInSave);
        
        //log.info("Processing {} records in {} batches of {} rows each", 
                 //partNumbers.size(), totalBatches, totalRowsInSave);
        
        // Process in batches
        for (int i = 0; i < partNumbers.size(); i += totalRowsInSave) {
            int endIndex = Math.min(i + totalRowsInSave, partNumbers.size());
            List<PartNumber> batch = partNumbers.subList(i, endIndex);
            
           
            try {
                // Process this batch in separate transaction
                UploadResult batchResult = processBatchInTransaction(batch);
                
                // Accumulate results
                finalResult.setNewRecords(finalResult.getNewRecords() + batchResult.getNewRecords());
                finalResult.setDuplicateRecords(finalResult.getDuplicateRecords() + batchResult.getDuplicateRecords());
                finalResult.setTotalProcessed(finalResult.getTotalProcessed() + batchResult.getTotalProcessed());
                
                // Merge skipped PNs
                if (batchResult.getSkippedPns() != null && !batchResult.getSkippedPns().isEmpty()) {
                    finalResult.getSkippedPns().addAll(batchResult.getSkippedPns());
                }
                
                
            } catch (Exception e) {
                
                // Continue with next batch for partial success
                // You might want to change this behavior based on requirements
            }
            
            batchNumber++;
        }
        
       
        return finalResult;
    }

    /**
     * Process a single batch in its own transaction
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private UploadResult processBatchInTransaction(List<PartNumber> batch) {
        return savePartNumbers(batch);
    }

    /**
     * Save part numbers to database with duplicate handling
     * This is the original method adapted for batch processing
     */
    private UploadResult savePartNumbers(List<PartNumber> partNumbers) {
        if (partNumbers.isEmpty()) {
            return new UploadResult(0, 0, 0, 0, Collections.emptyList());
        }

        // Get existing part numbers from database
        List<String> pnList = partNumbers.stream()
                .map(PartNumber::getPn)
                .collect(Collectors.toList());

        Map<String, PartNumber> existingPartsMap = partNumberRepository.findByPnIn(pnList)
                .stream()
                .collect(Collectors.toMap(PartNumber::getPn, Function.identity()));

        // Lists to track results
        List<PartNumber> newRecords = new ArrayList<>();
        List<PartNumber> recordsToUpdate = new ArrayList<>();
        List<String> duplicateRows = new ArrayList<>();

        for (PartNumber partNumber : partNumbers) {
            String pn = partNumber.getPn();
            
            if (existingPartsMap.containsKey(pn)) {
                // PN exists in database, check if entire row is identical
                PartNumber existingPart = existingPartsMap.get(pn);
                
                if (isIdenticalRow(existingPart, partNumber)) {
                    // Entire row is identical - count as duplicate
                	System.out.println("test ");
                    duplicateRows.add(pn);
                } else {
                    // Same PN but different data - update existing record
                    existingPart.setPnDesc(partNumber.getPnDesc());
                    existingPart.setUnit(partNumber.getUnit());
                    existingPart.setPrice(partNumber.getPrice());
                    existingPart.setTotalStock(partNumber.getTotalStock());
                    recordsToUpdate.add(existingPart);
                }
            } else {
                // New PN - save as new record
                newRecords.add(partNumber);
            }
        }

        // Save new records
        List<PartNumber> savedNewRecords = new ArrayList<>();
        if (!newRecords.isEmpty()) {
            savedNewRecords = partNumberRepository.saveAll(newRecords);
        }

        // Update existing records
        List<PartNumber> updatedRecords = new ArrayList<>();
        if (!recordsToUpdate.isEmpty()) {
            updatedRecords = partNumberRepository.saveAll(recordsToUpdate);
        }
        
        System.out.println("Input records: " + partNumbers.size());
        System.out.println("Duplicates in Excel: " + duplicateRows.size());
        System.out.println("New records: " + newRecords.size());  
        System.out.println("Records to update: " + recordsToUpdate.size());
        System.out.println("Actually saved: " + savedNewRecords.size());

        return new UploadResult(
                savedNewRecords.size(),     // newRecords
                recordsToUpdate.size(),      // updatedRecords  
                duplicateRows.size(),       // duplicateRecords
                partNumbers.size(),         // totalProcessed
                duplicateRows               // skippedPns
        );
    }

    /**
     * Helper method to compare if two PartNumber objects have identical data
     */
    private boolean isIdenticalRow(PartNumber existing, PartNumber incoming) {
        return Objects.equals(existing.getPn(), incoming.getPn()) &&
               Objects.equals(existing.getPnDesc(), incoming.getPnDesc()) &&
               Objects.equals(existing.getUnit(), incoming.getUnit()) &&
               Objects.equals(existing.getPrice(), incoming.getPrice()) &&
               Objects.equals(existing.getTotalStock(), incoming.getTotalStock());
    }
   
}

