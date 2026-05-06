package ma.scs.inventory_app.dto;
import lombok.Data;
import java.util.List;

/**
 * Result class for upload operations
 */
public class UploadResult {
    private int newRecords;
    private int duplicateRecords;
    private int totalProcessed;
    private int updatedRecords;
    private List<String> skippedPns;

    public UploadResult(int newRecords, int updatedRecords ,int duplicateRecords, int totalProcessed, List<String> skippedPns) {
        this.newRecords = newRecords;
        this.duplicateRecords = duplicateRecords;
        this.totalProcessed = totalProcessed;
        this.skippedPns = skippedPns;
        this.updatedRecords = updatedRecords;
    }

    // Getters
    public int getNewRecords() { return newRecords; }
    public int getDuplicateRecords() { return duplicateRecords; }
    public int getTotalProcessed() { return totalProcessed; }
    public int getUpdatedRecords() { return updatedRecords; }
    public List<String> getSkippedPns() { return skippedPns; }

    // Setters
    public void setNewRecords(int newRecords) { this.newRecords = newRecords; }
    public void setDuplicateRecords(int duplicateRecords) { this.duplicateRecords = duplicateRecords; }
    public void setUpdatedRecords(int updatedRecords) { this.updatedRecords = updatedRecords; }
    public void setTotalProcessed(int totalProcessed) { this.totalProcessed = totalProcessed; }
    public void setSkippedPns(List<String> skippedPns) { this.skippedPns = skippedPns; }
}
