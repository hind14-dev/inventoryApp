package ma.scs.inventory_app.dto;

public class TopAgentDTO {
    private String userName;
    private Long recordCount;
    private String areaName;

    public TopAgentDTO() {}

    public TopAgentDTO(String userName, Long recordCount, String areaName) {
        this.userName = userName;
        this.recordCount = recordCount;
        this.areaName = areaName;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getRecordCount() { return recordCount; }
    public void setRecordCount(Long recordCount) { this.recordCount = recordCount; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
}