package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}