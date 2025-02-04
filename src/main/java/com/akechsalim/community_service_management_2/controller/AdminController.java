package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserWithTasksDTO;
import com.akechsalim.community_service_management_2.model.TrainingProgress;
import com.akechsalim.community_service_management_2.service.TaskService;
import com.akechsalim.community_service_management_2.service.TrainingProgressService;
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
    private final TrainingProgressService trainingProgressService;

    public AdminController(UserService userService, TaskService taskService,TrainingProgressService trainingProgressService) {
        this.userService = userService;
        this.taskService = taskService;
        this.trainingProgressService = trainingProgressService;
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

    @GetMapping("/sponsors")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getSponsors() {
        return ResponseEntity.ok(userService.getSponsors());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String searchTerm) {
        return ResponseEntity.ok(userService.searchUsers(searchTerm));
    }
    @GetMapping("/dashboard/volunteer-progress")
    public ResponseEntity<List<TrainingProgress>> getVolunteerProgress() {
        List<TrainingProgress> progress = trainingProgressService.getAllProgress();
        return ResponseEntity.ok(progress);
    }
}