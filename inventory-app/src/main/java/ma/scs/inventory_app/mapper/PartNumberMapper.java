package ma.scs.inventory_app.mapper;

import ma.scs.inventory_app.dto.PartNumberDTO;
import ma.scs.inventory_app.entities.PartNumber;
import org.springframework.stereotype.Component;

@Component
public class PartNumberMapper {

    public static PartNumberDTO toDTO(PartNumber entity) {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setId(entity.getId());
        dto.setPn(entity.getPn());
        dto.setPnDesc(entity.getPnDesc());
        dto.setUnit(entity.getUnit());
        dto.setTotalStock(entity.getTotalStock());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    public static PartNumber toEntity(PartNumberDTO dto) {
        PartNumber entity = new PartNumber();
        entity.setId(dto.getId());
        entity.setPn(dto.getPn());
        entity.setPnDesc(dto.getPnDesc());
        entity.setUnit(dto.getUnit());
        entity.setTotalStock(dto.getTotalStock());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
