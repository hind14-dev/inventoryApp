package ma.scs.inventory_app.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.DataEntryDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.entities.BatchNumber;
import ma.scs.inventory_app.entities.DataEntry;
import ma.scs.inventory_app.entities.PartNumber;
import ma.scs.inventory_app.entities.User;
import ma.scs.inventory_app.repository.jpa.AreaRepository;
import ma.scs.inventory_app.repository.jpa.BatchNumberRepository;
import ma.scs.inventory_app.repository.jpa.PartNumberRepository;
import ma.scs.inventory_app.repository.jpa.UserRepository;


@Component
@RequiredArgsConstructor
public class DataEntryMapper {
	private final PartNumberRepository partNumberRepository;
	//private final AreaRepository areaRepository;
	//private final UserRepository userRepository;
	private final BatchNumberRepository batchNumberRepository;
	
	private static String generateDefaultBn() {
        LocalDate now = LocalDate.now();
        // Format month as two digits and year as four digits
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return "inv-" + now.format(formatter);
    }
	
    public static DataEntryDTO toDTO(DataEntry entity, PartNumberRepository partNumberRepository) {
        DataEntryDTO dto = new DataEntryDTO();
        dto.setId(entity.getId());
        dto.setSn(entity.getSn());
        dto.setPn((entity.getPn()).getPn());
        dto.setBn(entity.getBn());
        dto.setQuantity(entity.getQuantity());
        dto.setComment(entity.getComment());
        dto.setDateEntry(entity.getDateEntry());
        dto.setAreaId(entity.getArea().getAreaId());
        dto.setAreaName(entity.getArea() != null ? entity.getArea().getArea() : null);
        dto.setUserId(entity.getUser().getUserId());
        dto.setUserName(entity.getUser() != null ? entity.getUser().getFullName() : null);
        dto.setPcName(entity.getPcName());
        dto.setLastChange(entity.getLastChange());
        dto.setChangedBy(entity.getChangedBy());

        // ✅ Load description & unit from PartNumberRepository
        if (entity.getPn() != null) {
            partNumberRepository.findByPn((entity.getPn()).getPn()).ifPresent(part -> {
                dto.setDescription(part.getPnDesc());
                dto.setUnit(part.getUnit());
            });
        }

        return dto;
    }

    public DataEntry toEntity(DataEntryDTO dto, User user, Area area) {
    	
    	PartNumber pn = partNumberRepository.findByPn(dto.getPn()).orElse(null);
    	//Area areaVar = areaRepository.findByAreaId(dto.getAreaId()).orElse(null) ; 
    	//User userVar = userRepository.findByUserId(dto.getUserId()).orElse(null) ; 
    	BatchNumber defaultBN = batchNumberRepository.findFirstByIsDefaultBnTrue().orElse(null);
    	
    	DataEntry.DataEntryBuilder builder = DataEntry.builder()
                .id(dto.getId())
                .sn(dto.getSn())
                .pn(pn)
                .quantity(dto.getQuantity())
                .comment(dto.getComment())
                .area(area)
                .user(user)
                .pcName(dto.getPcName())
                .lastChange(dto.getLastChange())
                .changedBy(dto.getChangedBy());

        // Only set bn if DTO provides a value
        if(dto.getBn() != null){
        	builder.bn(dto.getBn());
        }else if (defaultBN != null) {
            
            builder.bn(defaultBN.getBn());
        }else {
        	builder.bn(generateDefaultBn());
        }
        
        return builder.build();
    }
}
