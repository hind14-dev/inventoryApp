package ma.scs.inventory_app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "password")
    private String password;
    @Column(name = "is_logged_in")
    private Boolean isLoggedIn;
    @Column(name = "role")
    private String role; // ADMIN or USER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_num", referencedColumnName = "area_id")
    @JsonBackReference
    private Area area;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isLoggedIn=" + isLoggedIn +
                ", role='" + role + '\'' +
                ", areaId=" + (area != null ? area.getAreaId() : null) +
                ", areaName=" + (area != null ? area.getArea() : null) +
                '}';
    }
}
