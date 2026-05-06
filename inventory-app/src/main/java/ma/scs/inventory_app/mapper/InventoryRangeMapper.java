package ma.scs.inventory_app.mapper;

import ma.scs.inventory_app.dto.InventoryRangeDTO;

import ma.scs.inventory_app.entities.Range;

public class InventoryRangeMapper {

    public static InventoryRangeDTO toDTO(Range entity) {
        if (entity == null) return null;

        InventoryRangeDTO dto = new InventoryRangeDTO();
        dto.setId(entity.getId());
        dto.setPrefix(entity.getPrefix());
        dto.setStartRange(entity.getStartRange());
        dto.setEndRange(entity.getEndRange());
        return dto;
    }

    public static Range toEntity(InventoryRangeDTO dto) {
        if (dto == null) return null;

        Range entity = new Range();
        entity.setId(dto.getId());
        entity.setPrefix(dto.getPrefix());
        entity.setStartRange(dto.getStartRange());
        entity.setEndRange(dto.getEndRange());
        return entity;
    }
}
