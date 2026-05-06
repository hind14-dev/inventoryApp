package ma.scs.inventory_app.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PartNumberDTO {
    private Long id;
    private String pn;
    private String pnDesc;
    private String unit;
    private BigDecimal totalStock;
    private BigDecimal price;
}
