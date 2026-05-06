package ma.scs.inventory_app.dto;

import lombok.Data;

@Data
public class AreaDTO {
    private Long id;
    private Long areaId;
    private String area;
    private String areaDesc;
    private Long oldAreaNo;

    private InventoryRangeDTO range;  // Embed the Range DTO
}
