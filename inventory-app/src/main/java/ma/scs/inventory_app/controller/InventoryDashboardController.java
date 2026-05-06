package ma.scs.inventory_app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ma.scs.inventory_app.dto.AreaRecordsDTO;
import ma.scs.inventory_app.dto.DashboardMetrics;
import ma.scs.inventory_app.dto.TopAgentDTO;
import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.repository.jpa.LiveDashboardDataRepository;
import ma.scs.inventory_app.service.InventoryRangeService;
import ma.scs.inventory_app.service.iml.DataEntryServiceImpl;
import ma.scs.inventory_app.service.iml.LiveDashboardService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/inventory-dashboard")
@CrossOrigin(origins = "*") // Configure for production
public class InventoryDashboardController {

    @Autowired
    private LiveDashboardService liveDashboardService;

   

    // REST endpoint for current metrics
   /* @GetMapping("/metrics")
    public DashboardMetrics getCurrentMetrics() {
        return liveDashboardService.getCurrentMetrics();
    }*/

    // Get recent activity count
    @GetMapping("/Records-num-in-last-day")
    public Long getRecentActivityCount() {
        
        return liveDashboardService.getRecordsNumInLastDay();
    }


    // Individual metric endpoints
    @GetMapping("/total-records")
    public Long getTotalRecords() {
        return liveDashboardService.getTotalRecords();
    }

    @GetMapping("/avg-per-day")
    public Double getAvgRecordsPerDay() {
        return liveDashboardService.getAvgRecordsPerDay();
    }

    @GetMapping("/top-agents")
    public List<TopAgentDTO> getTopAgents() {
        return liveDashboardService.getTopAgents();
    }

    @GetMapping("/records-per-area")
    public List<AreaRecordsDTO> getRecordsPerArea() {
        return liveDashboardService.getRecordsPerArea();
    }

    
   
}