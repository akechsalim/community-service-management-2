package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.dto.UserWithTasksDTO;
import com.akechsalim.community_service_management_2.exception.InvalidCredentialsException;
import com.akechsalim.community_service_management_2.exception.UserAlreadyExistsException;
import com.akechsalim.community_service_management_2.exception.UserNotFoundException;
import com.akechsalim.community_service_management_2.model.Role;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.AdminEmailRepository;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import com.akechsalim.community_service_management_2.security.CustomUserDetails;
import com.akechsalim.community_service_management_2.util.DtoMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminEmailRepository adminEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskService taskService;
    private final EmailService emailService;
    private final OtpService otpService;
    private final JavaMailSender mailSender;


    public UserService(UserRepository userRepository, AdminEmailRepository adminEmailRepository,
                       PasswordEncoder passwordEncoder, TaskService taskService,
                       EmailService emailService, OtpService otpService,JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.adminEmailRepository = adminEmailRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskService = taskService;
        this.emailService = emailService;
        this.otpService = otpService;
        this.mailSender = mailSender;
    }

    // Authentication-related methods
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findUserByUsernameOrThrow(username);
        return new CustomUserDetails(user);
    }

    public User validateUser(String username, String password) {
        User user = findUserByUsernameOrThrow(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password for username: " + username);
        }
        return user;
    }

    // User creation with OTP
    @Transactional
    public UserDTO createUser(UserRegisterDTO dto) {
        validateUserRegistration(dto);
        User user = createAndSaveUser(dto);
        sendOtpForUser(user);
        return DtoMapper.toUserDTO(user);
    }

    private void validateUserRegistration(UserRegisterDTO dto) {
        if (existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + dto.getUsername() + " already exists.");
        }
        validateAdminRegistration(dto);
    }

    private void validateAdminRegistration(UserRegisterDTO dto) {
        if ("ADMIN".equals(dto.getRole()) && !isAdminEmailAuthorized(dto.getEmail())) {
            throw new IllegalArgumentException("Email not authorized for admin registration");
        }
    }

    private boolean isAdminEmailAuthorized(String email) {
        return adminEmailRepository.existsById(email);
    }

    private User createAndSaveUser(UserRegisterDTO dto) {
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), Role.valueOf(dto.getRole().toString()));
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    private void sendOtpForUser(User user) {
        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    // User retrieval methods
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO findByUsername(String username) {
        User user = findUserByUsernameOrThrow(username);
        return DtoMapper.toUserDTO(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(DtoMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserWithTasksDTO> getVolunteersWithTasks() {
        return userRepository.findByRole(Role.VOLUNTEER).stream()
                .map(user -> {
                    List<TaskDTO> tasks = taskService.getTasksByVolunteerId(user.getId()).stream()
                            .map(task -> new TaskDTO(
                                    task.getId(),
                                    task.getTitle(),
                                    task.getDescription(),
                                    task.getStatus(),
                                    task.getVolunteer().getId()
                            ))
                            .collect(Collectors.toList());
                    return DtoMapper.toUserWithTasksDTO(user, tasks);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAvailableVolunteers() {
        return userRepository.findByRoleAndNoTasksOrPendingTasks(Role.VOLUNTEER).stream()
                .map(DtoMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getSponsors() {
        return userRepository.findByRole(Role.SPONSOR).stream()
                .map(DtoMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> searchUsers(String searchTerm) {
        return userRepository.findByUsernameContainingIgnoreCase(searchTerm).stream()
                .map(DtoMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    // User deletion
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Profile management methods
    @Transactional
    public void updateProfile(String currentUsername, String newUsername, String email) {
        User user = findUserByUsernameOrThrow(currentUsername);
        if (!newUsername.equals(currentUsername) && existsByUsername(newUsername)) {
            throw new UserAlreadyExistsException("Username " + newUsername + " is already taken.");
        }
        user.setUsername(newUsername);
        user.setEmail(email);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String username, String currentPassword, String newPassword) {
        User user = findUserByUsernameOrThrow(username);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Helper method
    private User findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void requestPasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        sendResetEmail(user.getEmail(), token);
    }

    private void sendResetEmail(String to, String token) throws MessagingException {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Password Reset Request");
        helper.setText("""
            <p>You requested a password reset. Click the link below to set a new password:</p>
            <p><a href="%s">Reset Password</a></p>
            <p>This link will expire in 1 hour.</p>
            <p>If you didn't request this, please ignore this email.</p>
            """.formatted(resetLink), true);

        mailSender.send(message);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return true;
    }
}