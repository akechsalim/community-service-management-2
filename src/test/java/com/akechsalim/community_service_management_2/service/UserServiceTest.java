//package com.akechsalim.community_service_management_2.service;
//
//import com.akechsalim.community_service_management_2.dto.UserDTO;
//import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
//import com.akechsalim.community_service_management_2.exception.InvalidCredentialsException;
//import com.akechsalim.community_service_management_2.exception.UserAlreadyExistsException;
//import com.akechsalim.community_service_management_2.exception.UserNotFoundException;
//import com.akechsalim.community_service_management_2.model.Role;
//import com.akechsalim.community_service_management_2.model.User;
//import com.akechsalim.community_service_management_2.repository.AdminEmailRepository;
//import com.akechsalim.community_service_management_2.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static com.akechsalim.community_service_management_2.model.Role.ADMIN;
//import static com.akechsalim.community_service_management_2.model.Role.VOLUNTEER;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AdminEmailRepository adminEmailRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private TaskService taskService;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private OtpService otpService;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        // No manual initialization needed with @InjectMocks
//    }
//
//    @Test
//    void testCreateUser() {
//        // Arrange
//        UserRegisterDTO dto = new UserRegisterDTO("testuser", "test@example.com", "password", VOLUNTEER);
//        User user = new User("testuser", "encodedPassword", VOLUNTEER);
//        user.setId(1L);
//        user.setEmail("test@example.com");
//
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(otpService.generateOtp("test@example.com")).thenReturn("123456");
//
//        // Act
//        UserDTO userDTO = userService.createUser(dto);
//
//        // Assert
//        assertEquals(1L, userDTO.getId());
//        assertEquals("testuser", userDTO.getUsername());
//        assertEquals(VOLUNTEER, userDTO.getRole());
//        verify(userRepository, times(1)).save(any(User.class));
//        verify(emailService, times(1)).sendOtpEmail("test@example.com", "123456");
//    }
//
//    @Test
//    void testCreateUserWhenUserExists() {
//        // Arrange
//        UserRegisterDTO dto = new UserRegisterDTO("testuser", "test@example.com", "password", VOLUNTEER);
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
//
//        // Act & Assert
//        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void testCreateUserAdminUnauthorizedEmail() {
//        // Arrange
//        UserRegisterDTO dto = new UserRegisterDTO("adminuser", "unauthorized@example.com", "password", ADMIN);
//        when(userRepository.findByUsername("adminuser")).thenReturn(Optional.empty());
//        when(adminEmailRepository.existsById("unauthorized@example.com")).thenReturn(false);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void testFindByUsername() {
//        // Arrange
//        User user = new User("testuser", "encodedPassword", VOLUNTEER);
//        user.setId(1L);
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//
//        // Act
//        UserDTO userDTO = userService.findByUsername("testuser");
//
//        // Assert
//        assertEquals(1L, userDTO.getId());
//        assertEquals("testuser", userDTO.getUsername());
//        assertEquals(VOLUNTEER, userDTO.getRole());
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testFindByUsernameNotFound() {
//        // Arrange
//        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("nonexistent"));
//    }
//
//    @Test
//    void testValidateUserValidCredentials() {
//        // Arrange
//        User user = new User("testuser", "encodedPassword", VOLUNTEER);
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
//
//        // Act
//        User validatedUser = userService.validateUser("testuser", "password");
//
//        // Assert
//        assertEquals("testuser", validatedUser.getUsername());
//        assertEquals(VOLUNTEER, validatedUser.getRole());
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testValidateUserInvalidCredentials() {
//        // Arrange
//        User user = new User("testuser", "encodedPassword", VOLUNTEER);
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
//
//        // Act & Assert
//        assertThrows(InvalidCredentialsException.class, () -> userService.validateUser("testuser", "wrongPassword"));
//        verify(userRepository, times(1)).findByUsername("testuser");
//    }
//
//    @Test
//    void testValidateUserUserNotFound() {
//        // Arrange
//        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UserNotFoundException.class, () -> userService.validateUser("nonexistent", "password"));
//        verify(userRepository, times(1)).findByUsername("nonexistent");
//    }
//}