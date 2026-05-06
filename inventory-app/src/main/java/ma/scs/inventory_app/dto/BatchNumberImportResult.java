package ma.scs.inventory_app.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.scs.inventory_app.entities.BatchNumber;


@Data
@NoArgsConstructor
public class BatchNumberImportResult {
    private List<BatchNumber> successfulImports = new ArrayList<>();
    private List<ImportError> errors = new ArrayList<>();
    private int totalProcessed = 0;
    
    public void addSuccess(BatchNumber batchNumber) {
        successfulImports.add(batchNumber);
        totalProcessed++;
    }
    
    public void addError(int rowNumber, String errorMessage) {
        errors.add(new ImportError(rowNumber, errorMessage));
        totalProcessed++;
    }
    
    public int getSuccessCount() {
        return successfulImports.size();
    }
    
    public int getErrorCount() {
        return errors.size();
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImportError {
        private int rowNumber;
        private String errorMessage;
    }
}
