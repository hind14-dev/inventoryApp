package ma.scs.inventory_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class LoginResponseDTO {
    private String message;
    private UserDTO user;
}
