package ma.scs.inventory_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private Integer startRange;
    private Integer endRange;
}
