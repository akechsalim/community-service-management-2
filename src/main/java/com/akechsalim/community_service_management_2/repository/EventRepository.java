package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}