package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
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
    public ResponseEntity<List<UserDTO>> getVolunteers() {
        List<UserDTO> volunteers = userService.getVolunteers();
        return ResponseEntity.ok(volunteers);
    }
    @PostMapping("/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TaskDTO> assignTaskToVolunteer(@RequestBody TaskDTO taskDTO) {
        TaskDTO assignedTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(assignedTask);
    }
}