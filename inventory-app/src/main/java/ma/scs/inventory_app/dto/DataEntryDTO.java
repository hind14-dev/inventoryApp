package ma.scs.inventory_app.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataEntryDTO {

    private Long id;

    private String sn;

    // PartNumber info
    private String pn;          // FK column value
    private String description; // from PartNumber
    private String unit;        // from PartNumber

    // User info
    private Long userId;        // FK column value
    private String userName;    // from User

    // Area info
    private Long areaId;        // FK column value
    private String areaName;    // from Area

    private String bn;          // batch number
    private Integer quantity;
    private String comment;

    private LocalDateTime dateEntry;

    private String pcName;
    private LocalDateTime lastChange;
    private String changedBy;

    // Optional: constructor for easy mapping
    public DataEntryDTO(Long id, String sn, String pn, String description, String unit,
                        String bn, Integer quantity, String comment,
                        LocalDateTime dateEntry, Long areaId, String areaName,
                        Long userId, String userName, String pcName,
                        LocalDateTime lastChange, String changedBy) {
        this.id = id;
        this.sn = sn;
        this.pn = pn;
        this.description = description;
        this.unit = unit;
        this.bn = bn;
        this.quantity = quantity;
        this.comment = comment;
        this.dateEntry = dateEntry;
        this.areaId = areaId;
        this.areaName = areaName;
        this.userId = userId;
        this.userName = userName;
        this.pcName = pcName;
        this.lastChange = lastChange;
        this.changedBy = changedBy;
    }
    
    public DataEntryDTO() {}
    
}


