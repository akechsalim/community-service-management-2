package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
    List<Sponsorship> findBySponsorId(Long sponsorId);

}
