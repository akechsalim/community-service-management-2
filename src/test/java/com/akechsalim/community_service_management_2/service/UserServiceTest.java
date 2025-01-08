package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.model.Role;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserRegisterDTO dto = new UserRegisterDTO("testuser", "password", Role.VOLUNTEER);
        User user = new User("testuser", "encodedPassword", Role.VOLUNTEER);
        user.setId(1L);

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = userService.createUser(dto);

        assertThat(userDTO.getId()).isEqualTo(1L);
        assertThat(userDTO.getUsername()).isEqualTo("testuser");
        assertThat(userDTO.getRole()).isEqualTo(Role.VOLUNTEER);
    }

    @Test
    void testCreateUserWhenUserExists() {
        UserRegisterDTO dto = new UserRegisterDTO("testuser", "password", Role.VOLUNTEER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    @Test
    void testValidateUserValidCredentials() {
        User user = new User("testuser", "encodedPassword", Role.VOLUNTEER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User validatedUser = userService.validateUser("testuser", "password");
        assertThat(validatedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void testValidateUserInvalidCredentials() {
        User user = new User("testuser", "encodedPassword", Role.VOLUNTEER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.validateUser("testuser", "wrongPassword"));
    }

    @Test
    void testValidateUserUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.validateUser("nonexistent", "password"));
    }
}