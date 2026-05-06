package ma.scs.inventory_app.controller;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.AreaDTO;
import ma.scs.inventory_app.service.AreaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    public ResponseEntity<Page<AreaDTO>> getAllAreas(
            @PageableDefault(size = 400, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(areaService.getAllAreas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDTO> getAreaById(@PathVariable Long id) {
        AreaDTO area = areaService.findByIdOptional(id);
        if (area != null) {
            return ResponseEntity.ok(area);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/byname/{areaName}")
    public ResponseEntity<AreaDTO> getAreaByName(@PathVariable String areaName) {
        try {
            AreaDTO area = areaService.getAreaByName(areaName);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            // Area not found
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<AreaDTO> createArea(@RequestBody AreaDTO areaDTO) {
        return ResponseEntity.ok(areaService.createArea(areaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaDTO> updateArea(@PathVariable Long id, @RequestBody AreaDTO areaDTO) {
        return ResponseEntity.ok(areaService.updateArea(id, areaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        areaService.deleteArea(id);
        return ResponseEntity.noContent().build();
    }
}
