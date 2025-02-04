package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.TrainingModuleDTO;
import com.akechsalim.community_service_management_2.model.TrainingModule;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.security.CustomUserDetails;
import com.akechsalim.community_service_management_2.service.TrainingModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-modules")
public class TrainingModuleController {

    private final TrainingModuleService trainingModuleService;

    public TrainingModuleController(TrainingModuleService trainingModuleService) {
        this.trainingModuleService = trainingModuleService;
    }

    @PostMapping
    public ResponseEntity<TrainingModule> createTrainingModule(
            @RequestBody TrainingModuleDTO trainingModuleDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // Get the logged-in user
        if (userDetails == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }
        User loggedInUser = userDetails.getUser(); // Extract User entity
        TrainingModule newModule = trainingModuleService.createTrainingModule(trainingModuleDTO, loggedInUser);
        return ResponseEntity.ok(newModule);
    }
    @GetMapping
    public ResponseEntity<List<TrainingModule>> getAllTrainingModules() {
        List<TrainingModule> modules = trainingModuleService.getAllTrainingModules();
        return ResponseEntity.ok(modules);
    }
    @GetMapping("/{moduleId}")
    public ResponseEntity<TrainingModule> getTrainingModuleById(@PathVariable Long moduleId) {
        TrainingModule module = trainingModuleService.getTrainingModuleById(moduleId);
        return ResponseEntity.ok(module);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<TrainingModule> updateTrainingModule(
            @PathVariable Long moduleId,
            @RequestBody TrainingModuleDTO trainingModuleDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }
        User loggedInUser = userDetails.getUser(); // Extract User entity
        TrainingModule updatedModule = trainingModuleService.updateTrainingModule(moduleId, trainingModuleDTO, loggedInUser);
        return ResponseEntity.ok(updatedModule);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteTrainingModule(
            @PathVariable Long moduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }
        trainingModuleService.deleteTrainingModule(moduleId);
        return ResponseEntity.noContent().build();
    }
}
