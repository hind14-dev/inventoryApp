package ma.scs.inventory_app.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private Boolean isLoggedIn;
    private String role;
    private String password;
    private Long areaId; // Reference only the areaId, not the full Area object
    private String areaName; // Name of the area
}