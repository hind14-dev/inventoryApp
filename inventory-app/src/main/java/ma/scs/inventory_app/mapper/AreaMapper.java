package ma.scs.inventory_app.mapper;

import ma.scs.inventory_app.dto.AreaDTO;
import ma.scs.inventory_app.dto.InventoryRangeDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.entities.Range;

public class AreaMapper {

    public static AreaDTO toDTO(Area entity) {
        if (entity == null) return null;

        AreaDTO dto = new AreaDTO();
        dto.setId(entity.getId());
        dto.setAreaId(entity.getAreaId());
        dto.setArea(entity.getArea());
        dto.setAreaDesc(entity.getAreaDesc());
        dto.setOldAreaNo(entity.getOldAreaNo());
        dto.setRange(InventoryRangeMapper.toDTO(entity.getRange())); // map Range entity to DTO
        return dto;
    }

    public static Area toEntity(AreaDTO dto) {
        if (dto == null) return null;

        Area entity = new Area();
        entity.setId(dto.getId());
        entity.setAreaId(dto.getAreaId());
        entity.setArea(dto.getArea());
        entity.setAreaDesc(dto.getAreaDesc());
        entity.setOldAreaNo(dto.getOldAreaNo());
        entity.setRange(InventoryRangeMapper.toEntity(dto.getRange())); // map RangeDTO to entity
        return entity;
    }
    public static Range toEntityRange(InventoryRangeDTO dto) {
        if (dto == null) return null;
        Range range = new Range();
        range.setId(dto.getId());
        range.setPrefix(dto.getPrefix());
        range.setStartRange(dto.getStartRange());
        range.setEndRange(dto.getEndRange());
        return range;
    }

}
