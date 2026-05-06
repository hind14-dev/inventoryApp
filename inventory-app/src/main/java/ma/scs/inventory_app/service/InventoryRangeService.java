package ma.scs.inventory_app.service;

import ma.scs.inventory_app.dto.InventoryRangeDTO;
import ma.scs.inventory_app.entities.Range;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryRangeService {
	Page<InventoryRangeDTO> getAllRanges(Pageable pageable);
    Range getRangeById(Long id);
    Range saveRange(Range range);
    Range updateRange(Long id, Range updatedRange);
    void deleteRange(Long id);
}
