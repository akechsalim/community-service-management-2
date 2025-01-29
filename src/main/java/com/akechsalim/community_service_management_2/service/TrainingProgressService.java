package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.model.TrainingModule;
import com.akechsalim.community_service_management_2.model.TrainingProgress;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.TrainingModuleRepository;
import com.akechsalim.community_service_management_2.repository.TrainingProgressRepository;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingProgressService {

    private final TrainingProgressRepository trainingProgressRepository;

    private final UserRepository userRepository;

    private final TrainingModuleRepository trainingModuleRepository;

    public TrainingProgressService(TrainingProgressRepository trainingProgressRepository, UserRepository userRepository, TrainingModuleRepository trainingModuleRepository) {
        this.trainingProgressRepository = trainingProgressRepository;
        this.userRepository = userRepository;
        this.trainingModuleRepository = trainingModuleRepository;
    }

    public TrainingProgress markModuleAsCompleted(Long volunteerId, Long moduleId) {
        User volunteer = userRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        TrainingModule module = trainingModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Training module not found"));

        TrainingProgress progress = new TrainingProgress();
        progress.setVolunteer(volunteer);
        progress.setModule(module);
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());

        return trainingProgressRepository.save(progress);
    }

    public List<TrainingProgress> getProgressByVolunteer(Long volunteerId) {
        return trainingProgressRepository.findByVolunteerId(volunteerId);
    }
}
