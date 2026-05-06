package ma.scs.inventory_app.service;

import ma.scs.inventory_app.dto.AreaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService {
    Page<AreaDTO> getAllAreas(Pageable pageable);
    AreaDTO getAreaById(Long id);
    AreaDTO getAreaByName(String AreaName);
    AreaDTO createArea(AreaDTO areaDTO);
    AreaDTO updateArea(Long id, AreaDTO areaDTO);
    void deleteArea(Long id);

    AreaDTO findByIdOptional(Long id);
}
