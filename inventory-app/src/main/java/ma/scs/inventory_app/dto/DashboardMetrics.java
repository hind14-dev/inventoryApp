package ma.scs.inventory_app.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardMetrics {
    private long totalRecords;
    private double avgRecordsPerMinute;
    private List<TopAgentDTO> topAgents;
    private List<AreaRecordsDTO> recordsPerArea;
    private LocalDateTime lastUpdated;

    // Constructors, getters, setters
    public DashboardMetrics() {
        this.lastUpdated = LocalDateTime.now();
    }

    public DashboardMetrics(long totalRecords, double avgRecordsPerMinute, 
                           List<TopAgentDTO> topAgents, List<AreaRecordsDTO> recordsPerArea) {
        this.totalRecords = totalRecords;
        this.avgRecordsPerMinute = avgRecordsPerMinute;
        this.topAgents = topAgents;
        this.recordsPerArea = recordsPerArea;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }

    public double getAvgRecordsPerMinute() { return avgRecordsPerMinute; }
    public void setAvgRecordsPerMinute(double avgRecordsPerMinute) { this.avgRecordsPerMinute = avgRecordsPerMinute; }

    public List<TopAgentDTO> getTopAgents() { return topAgents; }
    public void setTopAgents(List<TopAgentDTO> topAgents) { this.topAgents = topAgents; }

    public List<AreaRecordsDTO> getRecordsPerArea() { return recordsPerArea; }
    public void setRecordsPerArea(List<AreaRecordsDTO> recordsPerArea) { this.recordsPerArea = recordsPerArea; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}