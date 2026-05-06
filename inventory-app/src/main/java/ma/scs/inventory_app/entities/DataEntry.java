package ma.scs.inventory_app.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "InventoryData")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DataEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SN")
    private String sn;

    // Foreign key to PartNumber table
    @ManyToOne
    @JoinColumn(name = "pn", referencedColumnName = "id") // assuming PartNumber has 'pn' as primary/unique key
    private PartNumber pn;

    @Column(name = "bn", nullable = false)
    private String bn;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date_entry", nullable = false, insertable = false)
    private LocalDateTime dateEntry;

    // Foreign key to Area table
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    // Foreign key to User table
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "pc_name")
    private String pcName;

    @Column(name = "last_change")
    private LocalDateTime lastChange;

    @Column(name = "changed_by")
    private String changedBy;
}
