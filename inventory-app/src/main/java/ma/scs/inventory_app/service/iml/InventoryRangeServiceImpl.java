package ma.scs.inventory_app.service.iml;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.AreaDTO;
import ma.scs.inventory_app.dto.InventoryRangeDTO;
import ma.scs.inventory_app.entities.Range;
import ma.scs.inventory_app.mapper.AreaMapper;
import ma.scs.inventory_app.mapper.InventoryRangeMapper;
import ma.scs.inventory_app.repository.jpa.InventoryRangeRepository;
import ma.scs.inventory_app.service.InventoryRangeService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor

public class InventoryRangeServiceImpl implements InventoryRangeService {
	
    private final InventoryRangeRepository inventoryRangeRepository;
    
    
    public Page<InventoryRangeDTO> getAllRanges(Pageable pageable) {
        return inventoryRangeRepository.findAll(pageable)
                .map(InventoryRangeMapper::toDTO);
    }

    @Override
    public Range getRangeById(Long id) {
        return inventoryRangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Range not found with id: " + id));
    }

    @Override
    public Range saveRange(Range range) {
        return inventoryRangeRepository.save(range);
    }

    @Override
    public Range updateRange(Long id, Range updatedRange) {
    	// Find the existing range in the database
        Range existing = getRangeById(id);
        
        System.out.println("Received update for ID " + id + " with data: " + updatedRange);
        System.out.println("Existing range before update: " + existing);
        
        // Update the properties of the EXISTING object instead of replacing it
        existing.setPrefix(updatedRange.getPrefix());
        existing.setStartRange(updatedRange.getStartRange());
        existing.setEndRange(updatedRange.getEndRange());
        
        
        // Save the updated existing object
        Range savedRange = inventoryRangeRepository.save(existing);
        
        System.out.println("Successfully saved updated range: " + savedRange);
        return savedRange;
    }

    @Override
    public void deleteRange(Long id) {
        inventoryRangeRepository.deleteById(id);
    }
}
