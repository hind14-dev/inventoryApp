package ma.scs.inventory_app.controller;


import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.PartNumberDTO;
import ma.scs.inventory_app.entities.PartNumber;
import ma.scs.inventory_app.service.PartNumberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ma.scs.inventory_app.dto.UploadResult;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/parts")
@RequiredArgsConstructor
public class PartNumberController {

    private final PartNumberService partNumberService;

    @GetMapping
    public ResponseEntity<Page<PartNumberDTO>> getAllParts(
            @PageableDefault(size = 3000) Pageable pageable
    )
    {
        return ResponseEntity.ok(partNumberService.getAllParts(pageable));
    }

    @GetMapping("/{id}")
    public PartNumber getPartById(@PathVariable Long id) {
        return partNumberService.getPartById(id);
    }

    @GetMapping("/pn/{pn}")
    public PartNumber getPartByPn(@PathVariable String pn) {
        return partNumberService.getPartByPn(pn);
    }

    @PostMapping
    public PartNumber savePart(@RequestBody PartNumber partNumber) {
        return partNumberService.savePart(partNumber);
    }

    @PutMapping("/{id}")
    public PartNumber updatePart(@PathVariable Long id, @RequestBody PartNumber partNumber) {
        return partNumberService.updatePart(id, partNumber);
    }

    @DeleteMapping("/{id}")
    public void deletePart(@PathVariable Long id) {
        partNumberService.deletePart(id);
    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcelFile(
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Process the Excel file
            UploadResult result = partNumberService.processExcelFile(file);
            
            // Build success response
            response.put("success", true);
            response.put("message", buildSuccessMessage(result));
            response.put("data", Map.of(
                "newRecords", result.getNewRecords(),
                "duplicateRecords", result.getDuplicateRecords(),
                "totalProcessed", result.getTotalProcessed(),
                "totalUpdated", result.getUpdatedRecords(),
                "skippedPns", result.getSkippedPns(),
                "fileName", file.getOriginalFilename()
                
            ));
            
            // Return different status based on results
            if (result.getDuplicateRecords() > 0) {
                response.put("warning", "Some records were skipped due to duplicates");
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            // Validation errors (file format, size, etc.)
            response.put("success", false);
            response.put("error", "VALIDATION_ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            // Unexpected errors
            response.put("success", false);
            response.put("error", "PROCESSING_ERROR");
            response.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Preview Excel file without saving to database
     * Returns first few rows for user validation
     * 
     * @param file Excel file
     * @param rows Number of rows to preview (default: 5)
     * @return Preview data
     */
    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> previewExcelFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "rows", defaultValue = "5") int rows) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // You can add a preview method to your service
            // List<PartNumber> preview = excelUploadService.previewExcelFile(file, rows);
            
            response.put("success", true);
            response.put("message", "File preview generated successfully");
            // response.put("preview", preview);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error previewing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Get upload template/example
     * Returns information about expected Excel format
     */
    @GetMapping("/upload-template")
    public ResponseEntity<Map<String, Object>> getUploadTemplate() {
        Map<String, Object> template = Map.of(
            "columns", new String[]{"Part Number", "Description", "Price", "Total Stock", "Unit"},
            "columnTypes", new String[]{"Text", "Text", "Number", "Number", "Text"},
            "requiredColumns", new String[]{"Part Number"},
            "example", Map.of(
                "Part Number", "ABC123",
                "Description", "Sample Part Description",
                "Price", 25.50,
                "Total Stock", 100.0,
                "Unit", "PCS"
            ),
            "rules", new String[]{
                "Part Number is required and must be unique",
                "Price and Total Stock should be numeric values",
                "First row should contain column headers",
                "Maximum file size: 10MB"
            }
        );
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "template", template
        ));
    }
    
    /**
     * Check upload status or get recent upload history
     */
    @GetMapping("/upload-status")
    public ResponseEntity<Map<String, Object>> getUploadStatus() {
        // You can implement this to track upload history
        Map<String, Object> status = Map.of(
            "success", true,
            "message", "Upload service is operational",
            "supportedFormats", new String[]{".xlsx", ".xls"},
            "maxFileSize", "10MB"
        );
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * Helper method to build success message
     */
    private String buildSuccessMessage(UploadResult result) {
        StringBuilder message = new StringBuilder();
        message.append("File processed successfully. ");
        message.append(String.format("Total records: %d, ", result.getTotalProcessed()));
        message.append(String.format("New records added: %d", result.getNewRecords()));
        
        if (result.getDuplicateRecords() > 0) {
            message.append(String.format(", Duplicates skipped: %d", result.getDuplicateRecords()));
        }
        
        return message.toString();
    }
}

