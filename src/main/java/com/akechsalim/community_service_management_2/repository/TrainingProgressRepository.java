package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.TrainingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingProgressRepository extends JpaRepository<TrainingProgress, Long> {
    List<TrainingProgress> findByVolunteerId(Long volunteerId);
    List<TrainingProgress> findByModuleId(Long moduleId);
    Optional<TrainingProgress> findByVolunteerIdAndModuleId(Long volunteerId, Long moduleId);
}