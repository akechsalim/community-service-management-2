package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Task;
import com.akechsalim.community_service_management_2.model.TaskStatus;
import com.akechsalim.community_service_management_2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByVolunteerId(Long volunteerId);
    boolean existsByVolunteerIdAndStatus(Long volunteerId, TaskStatus status);
    List<Task> findByVolunteer(User volunteer);

}