package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByModuleId(Long moduleId);
}