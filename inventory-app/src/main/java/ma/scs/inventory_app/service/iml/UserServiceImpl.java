package ma.scs.inventory_app.service.iml;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.UserDTO;
import ma.scs.inventory_app.entities.Area;
import ma.scs.inventory_app.entities.User;
import ma.scs.inventory_app.mapper.UserMapper;
import ma.scs.inventory_app.repository.jpa.AreaRepository;
import ma.scs.inventory_app.repository.jpa.UserRepository;
import ma.scs.inventory_app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> {
                    UserDTO dto = UserMapper.toDTO(user);
                    return dto;
                });
    }


    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findByUserId(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        User user = UserMapper.toEntity(dto);
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findAreaById(dto.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Area not found"));
            user.setArea(area);
        }
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        System.out.println("id : " +id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setFullName(dto.getFullName());
        existing.setRole(dto.getRole());
        existing.setIsLoggedIn(dto.getIsLoggedIn());

        if (dto.getAreaId() != null) {
            Area area = areaRepository.findByAreaId(dto.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Area not found"));
            existing.setArea(area);
        }

        return UserMapper.toDTO(userRepository.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        return null;
    }
}