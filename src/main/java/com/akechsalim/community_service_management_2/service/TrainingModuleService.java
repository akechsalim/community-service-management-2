package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.TrainingModuleDTO;
import com.akechsalim.community_service_management_2.model.TrainingModule;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.TrainingModuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingModuleService {


    private final TrainingModuleRepository trainingModuleRepository;

    public TrainingModuleService(TrainingModuleRepository trainingModuleRepository) {
        this.trainingModuleRepository = trainingModuleRepository;
    }

    public TrainingModule createTrainingModule(TrainingModuleDTO trainingModuleDTO, User createdBy) {
        if (createdBy == null) {
            throw new IllegalArgumentException("Creator information is missing.");
        }
        TrainingModule trainingModule = new TrainingModule();
        trainingModule.setTitle(trainingModuleDTO.getTitle());
        trainingModule.setDescription(trainingModuleDTO.getDescription());
        trainingModule.setContent(trainingModuleDTO.getContent());
        trainingModule.setResourceUrl(trainingModuleDTO.getResourceUrl());
        trainingModule.setVideoUrl(trainingModuleDTO.getVideoUrl());
        trainingModule.setCreatedAt(LocalDateTime.now());
        trainingModule.setUpdatedAt(LocalDateTime.now());
        trainingModule.setCreatedBy(createdBy);

        return trainingModuleRepository.save(trainingModule);
    }
    public List<TrainingModule> getAllTrainingModules() {
        return trainingModuleRepository.findAll();
    }
}
