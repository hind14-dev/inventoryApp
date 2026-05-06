package ma.scs.inventory_app.controller;

import lombok.RequiredArgsConstructor;
import ma.scs.inventory_app.dto.LoginRequestDTO;
import ma.scs.inventory_app.dto.LoginResponseDTO;
import ma.scs.inventory_app.mapper.UserMapper;
import ma.scs.inventory_app.repository.jpa.UserRepository;
import ma.scs.inventory_app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}