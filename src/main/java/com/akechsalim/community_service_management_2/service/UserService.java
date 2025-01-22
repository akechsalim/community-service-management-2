package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.dto.UserWithTasksDTO;
import com.akechsalim.community_service_management_2.model.Role;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import com.akechsalim.community_service_management_2.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskService taskService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TaskService taskService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskService = taskService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
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

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public User validateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserWithTasksDTO> getVolunteersWithTasks() {
        List<User> volunteers = userRepository.findByRole(Role.VOLUNTEER);
        return volunteers.stream()
                .map(user -> {
                    UserWithTasksDTO dto = new UserWithTasksDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole());
                    // Use the correct method name from TaskService
                    dto.setTasks(taskService.getTasksByVolunteerId(user.getId()).stream()
                            .map(task -> new TaskDTO(
                                    task.getId(),
                                    task.getTitle(),
                                    task.getDescription(),
                                    task.getStatus(),
                                    task.getVolunteer().getId() // Assuming you want to include volunteerId in TaskDTO
                            ))
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<UserDTO> getAvailableVolunteers() {
        List<User> availableVolunteers = userRepository.findByRoleAndNoTasksOrPendingTasks(Role.VOLUNTEER);
        return availableVolunteers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<UserDTO> getSponsors() {
        return userRepository.findByRole(Role.SPONSOR).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private List<User> findByRoleAndNoTasksOrPendingTasks(Role role) {
        return userRepository.findByRoleAndNoTasksOrPendingTasks(role);
    }
    public List<UserDTO> searchUsers(String searchTerm) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(searchTerm);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }
}