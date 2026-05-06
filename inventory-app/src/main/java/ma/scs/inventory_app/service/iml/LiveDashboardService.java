package ma.scs.inventory_app.service.iml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.scs.inventory_app.dto.AreaRecordsDTO;
import ma.scs.inventory_app.dto.DashboardMetrics;
import ma.scs.inventory_app.dto.TopAgentDTO;
import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.repository.jpa.LiveDashboardDataRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class LiveDashboardService {

    @Autowired
    private LiveDashboardDataRepository liveDashboardDataRepository;

    

    /**
     * Get current metrics - SYNCHRONOUS JPA method
     */
    /*public DashboardMetrics getCurrentMetrics() {
        // Get all metrics from JPA repository (blocking calls)
        Long totalRecords = liveDashboardDataRepository.getTotalRecords().orElse(0L);
        Double avgRecordsPerMinute = liveDashboardDataRepository.getAvgRecordsPerDay().orElse(0.0);
        
        // Handle the single TopAgentDTO - convert to List for compatibility
        List<TopAgentDTO> topAgents = new ArrayList<>();
        liveDashboardDataRepository.getTopAgents().ifPresent(topAgents::add);
        
        // Handle the single AreaRecordsDTO - convert to List for compatibility
        List<AreaRecordsDTO> recordsPerArea = new ArrayList<>();
        liveDashboardDataRepository.getRecordsPerArea().ifPresent(recordsPerArea::add);

        // Calculate percentages for area records
        long totalAreaRecords = recordsPerArea.stream()
                .mapToLong(AreaRecordsDTO::getRecordCount)
                .sum();

        recordsPerArea.forEach(area -> {
            if (totalAreaRecords > 0) {
                double percentage = (area.getRecordCount() * 100.0) / totalAreaRecords;
                area.setPercentage(Math.round(percentage * 100.0) / 100.0);
            }
        });

        return new DashboardMetrics(totalRecords, avgRecordsPerMinute, topAgents, recordsPerArea);
    }*/

    /**
     * Get records count in last day - SYNCHRONOUS
     */
    public Long getRecordsNumInLastDay() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return liveDashboardDataRepository.getRecordsNumInLastDay(oneDayAgo).orElse(0L);
    }


    /**
     * Get total records count - SYNCHRONOUS
     */
    public Long getTotalRecords() {
        return liveDashboardDataRepository.getTotalRecords().orElse(0L);
    }

    /**
     * Get average records per minute - SYNCHRONOUS
     */
    public Double getAvgRecordsPerDay() {
        return liveDashboardDataRepository.getAvgRecordsPerDay().orElse(0.0);
    }

    /**
     * Get top agents - SYNCHRONOUS, returns single result or null
     */
    public List<TopAgentDTO> getTopAgents() {
        return liveDashboardDataRepository.getTopAgents();
    }

    /**
     * Get records per area - SYNCHRONOUS, returns single result or null
     */
    public List<AreaRecordsDTO> getRecordsPerArea() {
        List<AreaRecordsDTO> records = liveDashboardDataRepository.getRecordsPerArea();

        if (records == null || records.isEmpty()) {
            return Collections.emptyList();
        }

        long total = records.stream()
                            .mapToLong(AreaRecordsDTO::getRecordCount)
                            .sum();

        if (total > 0) {
            records.forEach(r -> {
                double percentage = (r.getRecordCount() * 100.0) / total;
                r.setPercentage(percentage);
            });
        }

        return records;
    }


    
}