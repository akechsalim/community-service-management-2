package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.EventDTO;
import com.akechsalim.community_service_management_2.dto.SponsorshipDTO;
import com.akechsalim.community_service_management_2.model.Sponsorship;
import com.akechsalim.community_service_management_2.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
//@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.status(201).body(eventService.createEvent(eventDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{eventId}/sponsor")
    @PreAuthorize("hasAuthority('ROLE_SPONSOR')")
    public ResponseEntity<Void> sponsorEvent(@PathVariable Long eventId, @RequestBody SponsorshipDTO sponsorshipDTO) {
        eventService.sponsorEvent(eventId, sponsorshipDTO.getAmount());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/sponsorships/sponsor/{sponsorId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<SponsorshipDTO>> getSponsorshipsBySponsor(@PathVariable Long sponsorId) {
        List<Sponsorship> sponsorships = eventService.getSponsorshipsBySponsor(sponsorId);
        List<SponsorshipDTO> sponsorshipDTOs = sponsorships.stream()
                .map(sp -> new SponsorshipDTO(sp.getAmount(), sp.getEvent().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(sponsorshipDTOs);
    }
}