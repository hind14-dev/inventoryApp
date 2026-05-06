package ma.scs.inventory_app.service.iml;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.LoginRequestDTO;
import ma.scs.inventory_app.dto.LoginResponseDTO;
import ma.scs.inventory_app.mapper.UserMapper;
import ma.scs.inventory_app.repository.jpa.UserRepository;
import ma.scs.inventory_app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO request) {
        System.out.println("Request : "+ request);
        System.out.println("IS Admin : " +request.getIsAdmin());
        return request.getIsAdmin() ? loginAsAdmin(request) : loginAsUser(request);
    }

    private ResponseEntity<LoginResponseDTO> loginAsAdmin(LoginRequestDTO request) {
        System.out.println("admin here");
        return userRepository.findByUserIdAndRole(request.getUserId(), "ADMIN")
                .filter(user -> Objects.equals(user.getPassword(), request.getPassword()))
                .map(user -> ResponseEntity.ok(new LoginResponseDTO("Admin login successful", userMapper.toDTO(user))))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO("Invalid admin credentials", null)));
    }

    private ResponseEntity<LoginResponseDTO> loginAsUser(LoginRequestDTO request) {
        return userRepository.findByUserIdAndArea_AreaId(request.getUserId(), request.getAreaId())
                .map(user -> ResponseEntity.ok(new LoginResponseDTO("User login successful", userMapper.toDTO(user))))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO("Invalid userId or area", null)));
    }
}
