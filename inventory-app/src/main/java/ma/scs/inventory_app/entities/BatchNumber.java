package ma.scs.inventory_app.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "batch_number")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String bn; // Original BN
    
    @Column(name = "is_default_bn", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean isDefaultBn ; // Default value
    
    @ManyToOne
    @JoinColumn(name = "pnumber_id", referencedColumnName = "id")
    private PartNumber partNumber;
}