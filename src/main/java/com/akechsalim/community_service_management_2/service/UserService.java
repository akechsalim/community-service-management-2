package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO createUser(UserRegisterDTO dto) {
        if (existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("User with username " + dto.getUsername() + " already exists.");
        }
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getRole());
        return convertToDTO(userRepository.save(user));
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO findByUsername(String username) {
        return convertToDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)));
    }

    public User validateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }
}