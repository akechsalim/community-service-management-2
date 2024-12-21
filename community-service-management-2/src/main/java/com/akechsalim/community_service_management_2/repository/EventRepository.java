package com.akechsalim.community_service_management_2.repository;

import com.akechsalim.community_service_management_2.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
}