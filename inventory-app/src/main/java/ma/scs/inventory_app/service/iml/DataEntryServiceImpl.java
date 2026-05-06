package ma.scs.inventory_app.service.iml;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.AreaRecordsDTO;
import ma.scs.inventory_app.dto.DashboardMetrics;
import ma.scs.inventory_app.dto.DataEntryDTO;
import ma.scs.inventory_app.dto.TopAgentDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.entities.Range;
import ma.scs.inventory_app.entities.User;
import ma.scs.inventory_app.mapper.DataEntryMapper;
import ma.scs.inventory_app.repository.jpa.AreaRepository;
import ma.scs.inventory_app.repository.jpa.DataEntryRepository;
import ma.scs.inventory_app.repository.jpa.LiveDashboardDataRepository;
import ma.scs.inventory_app.repository.jpa.PartNumberRepository;
import ma.scs.inventory_app.repository.jpa.UserRepository;
import ma.scs.inventory_app.service.DataEntryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataEntryServiceImpl implements DataEntryService {
	
	private final DataEntryMapper dataEntryMapper;
    private final DataEntryRepository dataEntryRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final PartNumberRepository partNumberRepository;

    @Override
    public Page<DataEntryDTO> getAllEntries(Pageable pageable) {
        return dataEntryRepository.findAll(pageable)
                .map(entry -> DataEntryMapper.toDTO(entry, partNumberRepository));
    }

    @Override
    public Page<DataEntry> getAllEntities(Pageable pageable) {
        return dataEntryRepository.findAll(pageable);
    }

    @Override
    public DataEntry getEntryById(Long id) {
        return dataEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DataEntry not found with id: " + id));
    }

    @Override
    public DataEntry saveEntry(DataEntryDTO dto) {
        validateSerialNumber(dto.getSn(), dto.getAreaId());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id:" + dto.getUserId()));

        Area area = areaRepository.findByAreaId(dto.getAreaId())
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + dto.getAreaId()));

        DataEntry entity = dataEntryMapper.toEntity(dto, user, area);
        return dataEntryRepository.save(entity);
    }

    @Override
    public DataEntry updateEntry(Long id, DataEntryDTO dto) {
        DataEntry existing = getEntryById(id);

        // If SN changed, validate it; else, skip validation for unchanged SN
        if (!existing.getSn().equals(dto.getSn())) {
            validateSerialNumber(dto.getSn(), dto.getAreaId());
        }

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Area area = areaRepository.findByAreaId(dto.getAreaId())
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + dto.getAreaId()));

        DataEntry updatedEntity = dataEntryMapper.toEntity(dto, user, area);
        updatedEntity.setId(existing.getId());

        return dataEntryRepository.save(updatedEntity);
    }

    @Override
    public void deleteEntry(Long id) {
        dataEntryRepository.deleteById(id);
    }
    
    @Override
    public List<DataEntryDTO> getEntriesByUser(Long Userid){
    	return dataEntryRepository.findByUser_id(Userid)
    			.stream()
                .map(entry -> DataEntryMapper.toDTO(entry, partNumberRepository))// assuming you have a mapper method
                .toList();
    	
    }

    private void validateSerialNumber(String sn, Long areaId) {
        // 1. Check if SN already exists in DB
        boolean exists = dataEntryRepository.findBySn(sn).isPresent();
        if (exists) {
            throw new RuntimeException("Serial Number '" + sn + "' already exists.");
        }

        // 2. Validate format
        if (sn == null || sn.length() < 2) {
            throw new RuntimeException("Invalid Serial Number format.");
        }

        // 3. Extract prefix (non-digit chars at start) and numeric part
        String prefix = sn.replaceAll("[0-9]", "");
        String numericPartStr = sn.replaceAll("[^0-9]", "");

        int numericPart;
        try {
            numericPart = Integer.parseInt(numericPartStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid numeric part in Serial Number.");
        }

        // 4. Get Area and its Range
        Area area = areaRepository.findByAreaId(areaId)
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + areaId));

        Range range = area.getRange();
        if (range == null) {
            throw new RuntimeException("No range associated with Area ID: " + areaId);
        }

        // 5. Check prefix matches
        if (!range.getPrefix().equalsIgnoreCase(prefix)) {
            throw new RuntimeException("Prefix '" + prefix + "' does not match the allowed prefix '" + range.getPrefix() + "' for this area.");
        }

        // 6. Check numeric part is within range
        if (numericPart < range.getStartRange() || numericPart > range.getEndRange()) {
            throw new RuntimeException("Serial Number numeric part '" + numericPart + "' is outside allowed range " + range.getStartRange() + " to " + range.getEndRange() + ".");
        }
    }
    
    
}
