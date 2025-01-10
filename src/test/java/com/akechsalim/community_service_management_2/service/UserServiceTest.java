package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.model.Role;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
//        userService = new UserService(userRepository, passwordEncoder);
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

        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals(Role.VOLUNTEER, userDTO.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWhenUserExists() {
        UserRegisterDTO dto = new UserRegisterDTO("testuser", "password", Role.VOLUNTEER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    @Test
    void testFindByUsername() {
        User user = new User("testuser", "password", Role.VOLUNTEER);
        user.setId(1L);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.findByUsername("testuser");

        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals(Role.VOLUNTEER, userDTO.getRole());
    }

    @Test
    void testValidateUserValidCredentials() {
        User user = new User("testuser", "encodedPassword", Role.VOLUNTEER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User validatedUser = userService.validateUser("testuser", "password");
        assertEquals("testuser", validatedUser.getUsername());
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