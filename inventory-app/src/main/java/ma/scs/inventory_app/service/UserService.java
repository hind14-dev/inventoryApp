package ma.scs.inventory_app.service;

import ma.scs.inventory_app.dto.UserDTO;
import ma.scs.inventory_app.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO login(UserDTO userDTO);
}