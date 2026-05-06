package ma.scs.inventory_app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequestDTO {
    private Long userId;
    private String password;  // nullable for non-admins
    private Long areaId;      // nullable for admins
    private Boolean isAdmin;
}
