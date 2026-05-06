package ma.scs.inventory_app.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;


@Entity
@Table(name = "part_number")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String pn; // Original PN

    private String pnDesc;

    private String unit;

    @Column(precision = 20, scale = 10)
    private BigDecimal totalStock;

    @Column(precision = 30, scale = 20)
    private BigDecimal price;
}

