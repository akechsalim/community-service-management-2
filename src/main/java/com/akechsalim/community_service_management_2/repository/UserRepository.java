package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Role;
import com.akechsalim.community_service_management_2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND (SELECT COUNT(t) FROM Task t WHERE t.volunteer = u AND t.status != 'PENDING') = 0")
    List<User> findByRoleAndNoTasksOrPendingTasks(@Param("role") Role role);

    List<User> findByUsernameContainingIgnoreCase(String username);

}