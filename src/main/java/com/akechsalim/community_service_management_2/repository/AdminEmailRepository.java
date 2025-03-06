package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.AdminEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEmailRepository extends JpaRepository<AdminEmail, String> {
}