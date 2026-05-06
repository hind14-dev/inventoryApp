package ma.scs.inventory_app.service.iml;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.AreaDTO;
import ma.scs.inventory_app.dto.UserDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.exception.RangeAlreadyAssignedException;
import ma.scs.inventory_app.mapper.AreaMapper;
import ma.scs.inventory_app.mapper.UserMapper;
import ma.scs.inventory_app.repository.jpa.AreaRepository;
import ma.scs.inventory_app.service.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;

    @Override
    public Page<AreaDTO> getAllAreas(Pageable pageable) {
        return areaRepository.findAll(pageable)
                .map(area -> {
                    AreaDTO dto = AreaMapper.toDTO(area);
                    return dto;
                });
    }

    @Override
    public AreaDTO getAreaById(Long id) {
        return areaRepository.findById(id)
                .map(AreaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Area not found"));
    }
    
    
    public AreaDTO findByIdOptional(Long id) {
        return areaRepository.findById(id)
                .map(AreaMapper::toDTO)
                .orElse(null);
    }
    
    
    @Override
    public AreaDTO getAreaByName(String areaName) {
        return areaRepository.findByArea(areaName)
                .map(AreaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Area not found with name: " + areaName));
    }
    
    
    @Override
    public AreaDTO createArea(AreaDTO areaDTO) {
        if (areaDTO.getRange() != null && areaDTO.getRange().getId() != null) {
            boolean exists = areaRepository.existsByRangeIdAndIdNot(areaDTO.getRange().getId(), null);
            if (exists) {
                throw new RangeAlreadyAssignedException("This range is already assigned to another area. Please choose a different one.");
            }
        }
        Area area = AreaMapper.toEntity(areaDTO);
        return AreaMapper.toDTO(areaRepository.save(area));
    }

    @Override
    public AreaDTO updateArea(Long id, AreaDTO areaDTO) {
        Area existing = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        if (areaDTO.getRange() != null && areaDTO.getRange().getId() != null) {
            boolean exists = areaRepository.existsByRangeIdAndIdNot(areaDTO.getRange().getId(), id);
            if (exists) {
                throw new RangeAlreadyAssignedException("This range is already assigned to another area. Please choose a different one.");
            }
        }

        existing.setAreaId(areaDTO.getAreaId());
        existing.setArea(areaDTO.getArea());
        existing.setAreaDesc(areaDTO.getAreaDesc());
        existing.setOldAreaNo(areaDTO.getOldAreaNo());
        existing.setRange(AreaMapper.toEntityRange(areaDTO.getRange())); // Make sure you have this mapper method

        return AreaMapper.toDTO(areaRepository.save(existing));
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }
}