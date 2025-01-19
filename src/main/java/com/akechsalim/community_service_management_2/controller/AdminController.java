package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserWithTasksDTO;
import com.akechsalim.community_service_management_2.service.TaskService;
import com.akechsalim.community_service_management_2.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    public AdminController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/volunteers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserWithTasksDTO>> getVolunteersWithTasks() {
        List<UserWithTasksDTO> volunteers = userService.getVolunteersWithTasks();
        return ResponseEntity.ok(volunteers);
    }
    @PostMapping("/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaskDTO> assignTaskToVolunteer(@RequestBody TaskDTO taskDTO) {
        TaskDTO assignedTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(assignedTask);
    }
    @GetMapping("/available-volunteers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAvailableVolunteers() {
        List<UserDTO> availableVolunteers = userService.getAvailableVolunteers();
        return ResponseEntity.ok(availableVolunteers);
    }
    @PostMapping("/tasks/{taskId}/complete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId) {
        taskService.completeTask(taskId);
        return ResponseEntity.ok().build();
    }
}