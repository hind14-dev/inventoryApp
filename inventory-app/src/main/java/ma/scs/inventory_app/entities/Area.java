package ma.scs.inventory_app.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "areas")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_id", nullable = false, unique = true)
    private Long areaId;

    @Column(nullable = false)
    private String area;

    @Column(name = "area_desc")
    private String areaDesc;

    @Column(name = "old_area_no")
    private Long oldAreaNo;

    @OneToMany(mappedBy = "area")
    @JsonManagedReference
    private List<User> users;

    @OneToOne
    @JoinColumn(name = "range_id", unique = true)
    private Range range;

    @Override
    public String toString() {
        return "Area{" +
                "areaId=" + areaId +
                ", area='" + area + '\'' +
                '}';
    }
}
