package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.model.TrainingProgress;
import com.akechsalim.community_service_management_2.service.TrainingProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-progress")
public class TrainingProgressController {

    private final TrainingProgressService trainingProgressService;

    public TrainingProgressController(TrainingProgressService trainingProgressService) {
        this.trainingProgressService = trainingProgressService;
    }

    @PostMapping
    public ResponseEntity<TrainingProgress> markModuleAsCompleted(@RequestParam Long volunteerId, @RequestParam Long moduleId) {
        TrainingProgress progress = trainingProgressService.markModuleAsCompleted(volunteerId, moduleId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<TrainingProgress>> getProgressByVolunteer(@PathVariable Long volunteerId) {
        List<TrainingProgress> progress = trainingProgressService.getProgressByVolunteer(volunteerId);
        return ResponseEntity.ok(progress);
    }
    @PutMapping("/progress/{progressId}/approve-certificate")
    public ResponseEntity<TrainingProgress> approveCertificateDownload(@PathVariable Long progressId) {
        TrainingProgress progress = trainingProgressService.approveCertificateDownload(progressId);
        return ResponseEntity.ok(progress);
    }
}
