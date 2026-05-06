package ma.scs.inventory_app.mapper;

import ma.scs.inventory_app.dto.BatchNumberDTO;
import ma.scs.inventory_app.entities.BatchNumber;
import ma.scs.inventory_app.entities.PartNumber;


public class BatchNumberMapper {
    /*public static BatchNumberDTO toDTO(BatchNumber entity) {
        BatchNumberDTO dto = new BatchNumberDTO();
        dto.setId(entity.getId());
        dto.setBn(entity.getBn());
        dto.setDefaultBn(entity.isDefaultBn());
        return dto;
    }

    public static BatchNumber toEntity(BatchNumberDTO dto) {
        BatchNumber entity = new BatchNumber();
        entity.setId(dto.getId());
        entity.setBn(dto.getBn());
        entity.setDefaultBn(dto.isDefaultBn());
        return entity;
    }*/
	
	public static BatchNumberDTO toDTO(BatchNumber entity) {
        BatchNumberDTO dto = new BatchNumberDTO();
        dto.setId(entity.getId());
        dto.setBn(entity.getBn());
        dto.setDefaultBn(entity.isDefaultBn());

        if (entity.getPartNumber() != null) {
            dto.setPartNumberId(entity.getPartNumber().getId());
        }

        return dto;
    }

    public static BatchNumber toEntity(BatchNumberDTO dto) {
        BatchNumber entity = new BatchNumber();
        entity.setId(dto.getId());
        entity.setBn(dto.getBn());
        entity.setDefaultBn(dto.isDefaultBn());

        if (dto.getPartNumberId() != null) {
            PartNumber pn = new PartNumber();
            pn.setId(dto.getPartNumberId()); // only set ID, no DB fetch here
            entity.setPartNumber(pn);
        }

        return entity;
    }
}
