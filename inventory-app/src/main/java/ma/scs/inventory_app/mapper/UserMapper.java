package ma.scs.inventory_app.mapper;

import ma.scs.inventory_app.dto.UserDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setIsLoggedIn(user.getIsLoggedIn());
        dto.setRole(user.getRole());
        dto.setPassword(user.getPassword() != null ? user.getPassword() : null);
        dto.setAreaId(user.getArea() != null ? user.getArea().getAreaId() : null);
        dto.setAreaName(user.getArea() != null ? user.getArea().getArea() : null);
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUserId(dto.getUserId());
        user.setFullName(dto.getFullName());
        user.setIsLoggedIn(dto.getIsLoggedIn());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        // Set area reference if areaId exists
        if (dto.getAreaId() != null) {
            Area area = new Area();
            area.setAreaId(dto.getAreaId());
            user.setArea(area);
        }

        return user;
    }
}
