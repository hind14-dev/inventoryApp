package ma.scs.inventory_app.controller;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.BatchNumberDTO;
import ma.scs.inventory_app.dto.BatchNumberImportResponse;
import ma.scs.inventory_app.dto.BatchNumberImportResult;
import ma.scs.inventory_app.dto.PartNumberDTO;
import ma.scs.inventory_app.entities.BatchNumber;
import ma.scs.inventory_app.entities.PartNumber;
import ma.scs.inventory_app.service.BatchNumberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/batch-numbers")
@RequiredArgsConstructor
public class BatchNumberController {
    private final BatchNumberService batchNumberService;

    @GetMapping
    public ResponseEntity<Page<BatchNumber>> getAllBNs(
            @PageableDefault(size = 30000) Pageable pageable
    )
    {
        return ResponseEntity.ok(batchNumberService.getAllBatchNumbers(pageable));
    }
    @GetMapping("/{id}")
    public BatchNumber getBatchById(@PathVariable Long id) {
        return batchNumberService.getBatchById(id);
    }
    
    
    
    @GetMapping("/DefaultBn")
    public BatchNumber getDefaultBatch() {
        return batchNumberService.getDefaultBatchNumber();
    }
    
    @PostMapping("/saveDefaultBn")
    public BatchNumber saveDefaultBatch(@RequestBody BatchNumber batchNumber) {
    	batchNumber.setId(null);
        return batchNumberService.saveDefaultBatch(batchNumber);
    }


    @GetMapping("/bn/{bn}")
    public BatchNumber getBatchByBn(@PathVariable String bn) {
        return batchNumberService.getBatchByBn(bn);
    }

    @PostMapping
    public BatchNumber saveBatch(@RequestBody BatchNumber batchNumber) {
    	batchNumber.setId(null);
        return batchNumberService.saveBatch(batchNumber);
    }

    @PutMapping("/{id}")
    public BatchNumber updateBatch(@PathVariable Long id, @RequestBody BatchNumber batchNumber) {
        return batchNumberService.updateBatch(id, batchNumber);
    }

    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable Long id) {
        batchNumberService.deleteBatch(id);
    }
    
    
    @PostMapping("/import-excel")
    public ResponseEntity<BatchNumberImportResponse> importBatchNumbersFromExcel(
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new BatchNumberImportResponse("File cannot be empty", null));
            }
            
            // Check file extension
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.toLowerCase().endsWith(".xlsx") && 
                                   !filename.toLowerCase().endsWith(".xls"))) {
                return ResponseEntity.badRequest()
                    .body(new BatchNumberImportResponse("File must be an Excel file (.xlsx or .xls)", null));
            }
            
            // Import batch numbers
            BatchNumberImportResult result = batchNumberService.importBatchNumbersFromExcel(file);
            
            // Prepare response
            String message = String.format("Import completed. Success: %d, Errors: %d", 
                                         result.getSuccessCount(), result.getErrorCount());
            
            BatchNumberImportResponse response = new BatchNumberImportResponse(message, result);
            
            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            } else {
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BatchNumberImportResponse("Import failed: " + e.getMessage(), null));
        }
    }
}

