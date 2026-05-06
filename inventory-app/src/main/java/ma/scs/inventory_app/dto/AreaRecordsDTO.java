package ma.scs.inventory_app.dto;

public class AreaRecordsDTO {
    private String areaName;
    private Long recordCount;
    private Double percentage;

    public AreaRecordsDTO() {}

    public AreaRecordsDTO(String areaName, Long recordCount) {
        this.areaName = areaName;
        this.recordCount = recordCount;
    }

    // Getters and Setters
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public Long getRecordCount() { return recordCount; }
    public void setRecordCount(Long recordCount) { this.recordCount = recordCount; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}

