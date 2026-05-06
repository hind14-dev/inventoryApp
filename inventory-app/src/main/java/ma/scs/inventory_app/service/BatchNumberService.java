package ma.scs.inventory_app.service;


import ma.scs.inventory_app.dto.BatchNumberDTO;
import ma.scs.inventory_app.dto.BatchNumberImportResult;
import ma.scs.inventory_app.entities.BatchNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BatchNumberService {
    Page<BatchNumber> getAllBatchNumbers(Pageable pageable);
    BatchNumber getDefaultBatchNumber();
    BatchNumber getBatchById(Long id);
    BatchNumber getBatchByBn(String bn);
    BatchNumber saveBatch(BatchNumber batchNumber);
    public BatchNumber saveDefaultBatch(BatchNumber batchNumber);
    BatchNumber updateBatch(Long id, BatchNumber updatedBatch);
    void deleteBatch(Long id);
    BatchNumberImportResult importBatchNumbersFromExcel(MultipartFile file);
}
