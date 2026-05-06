package ma.scs.inventory_app.dto;

import lombok.Data;

@Data
public class InventoryRangeDTO {
    private Long id;
    private String prefix;
    private Integer startRange;
    private Integer endRange;
}
