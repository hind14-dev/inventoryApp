package ma.scs.inventory_app.controller;


import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.InventoryRangeDTO;
import ma.scs.inventory_app.entities.Range;
import ma.scs.inventory_app.service.InventoryRangeService;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranges")
@RequiredArgsConstructor
public class InventoryRangeController {
    private final InventoryRangeService inventoryRangeService;
    
    
    @GetMapping
    public ResponseEntity<Page<InventoryRangeDTO>> getAllRanges(
            @PageableDefault(size = 10000) Pageable pageable) {
        Page<InventoryRangeDTO> ranges = inventoryRangeService.getAllRanges(pageable);
        return ResponseEntity.ok(ranges);
    }
    
    @GetMapping("/{id}")
    public Range getRangeById(@PathVariable Long id) {
    	return inventoryRangeService.getRangeById(id);
    }

    @PostMapping
    public Range saveRange(@RequestBody Range range) {
        return inventoryRangeService.saveRange(range);
    }

    @PutMapping("/{id}")
    public Range updateRange(@PathVariable Long id, @RequestBody Range range) {
    	System.out.println("Received update for ID " + id + " with data: " + range);
         Range updatedRange = inventoryRangeService.updateRange(id, range);
        System.out.println("Returning updated range: " + updatedRange);
        return updatedRange;
    }

    @DeleteMapping("/{id}")
    public void deleteRange(@PathVariable Long id) {
        inventoryRangeService.deleteRange(id);
    }
}
