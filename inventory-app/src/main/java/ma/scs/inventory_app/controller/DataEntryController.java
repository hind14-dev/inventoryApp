package ma.scs.inventory_app.controller;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.DataEntryDTO;
import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.mapper.DataEntryMapper;
import ma.scs.inventory_app.repository.jpa.PartNumberRepository;
import ma.scs.inventory_app.service.DataEntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/data-entries")
@RequiredArgsConstructor
public class DataEntryController {

    private final DataEntryService dataEntryService;
    private final PartNumberRepository partNumberRepository;

    @GetMapping
    public ResponseEntity<Page<DataEntryDTO>> getAllEntries(
            @PageableDefault(size = 40000, page = 0) Pageable pageable
    ) {
        Page<DataEntry> page = dataEntryService.getAllEntities(pageable);
        Page<DataEntryDTO> dtoPage = page.map(entry -> DataEntryMapper.toDTO(entry, partNumberRepository));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataEntryDTO> getEntryById(@PathVariable Long id) {
        DataEntry entry = dataEntryService.getEntryById(id);
        return ResponseEntity.ok(DataEntryMapper.toDTO(entry, partNumberRepository));
    }
    
    
    @GetMapping("/By-User/{Userid}")
    public ResponseEntity<List<DataEntryDTO>> getEntryByUserId(@PathVariable Long Userid) {
        List<DataEntryDTO> UserEntries = dataEntryService.getEntriesByUser(Userid);
        return ResponseEntity.ok(UserEntries);
    }
     

    @PostMapping
    public ResponseEntity<DataEntryDTO> saveEntry(@RequestBody DataEntryDTO dto) {
        DataEntry saved = dataEntryService.saveEntry(dto);
        return ResponseEntity.ok(DataEntryMapper.toDTO(saved, partNumberRepository));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataEntryDTO> updateEntry(@PathVariable Long id, @RequestBody DataEntryDTO dto) {
        DataEntry updated = dataEntryService.updateEntry(id, dto);
        return ResponseEntity.ok(DataEntryMapper.toDTO(updated, partNumberRepository));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        dataEntryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
