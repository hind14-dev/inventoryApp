package ma.scs.inventory_app.service;

import ma.scs.inventory_app.dto.LoginRequestDTO;
import ma.scs.inventory_app.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<LoginResponseDTO> login(LoginRequestDTO request);
}