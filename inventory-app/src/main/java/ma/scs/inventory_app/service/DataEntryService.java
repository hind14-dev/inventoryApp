package ma.scs.inventory_app.service;



import ma.scs.inventory_app.dto.DataEntryDTO;
import ma.scs.inventory_app.entities.DataEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataEntryService {

    Page<DataEntryDTO> getAllEntries(Pageable pageable);
    Page<DataEntry> getAllEntities(Pageable pageable);
    List<DataEntryDTO> getEntriesByUser(Long id);
    
    DataEntry getEntryById(Long id);

    DataEntry saveEntry(DataEntryDTO dto);

    DataEntry updateEntry(Long id, DataEntryDTO dto);

    void deleteEntry(Long id);
    
}

