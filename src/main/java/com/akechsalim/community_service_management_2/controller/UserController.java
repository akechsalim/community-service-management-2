package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.PasswordUpdateDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Existing endpoints
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // New profile management endpoints
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userService.findByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(
            @Valid @RequestBody UserDTO userDTO,
            Authentication authentication
    ) {
        String username = authentication.getName();
        userService.updateProfile(username, userDTO.getUsername(), userDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody PasswordUpdateDTO passwordDTO,
            Authentication authentication
    ) {
        String username = authentication.getName();
        userService.updatePassword(
                username,
                passwordDTO.getCurrentPassword(),
                passwordDTO.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}