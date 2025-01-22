package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.EventDTO;
import com.akechsalim.community_service_management_2.model.Event;
import com.akechsalim.community_service_management_2.model.Sponsorship;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.EventRepository;
import com.akechsalim.community_service_management_2.repository.SponsorshipRepository;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SponsorshipRepository sponsorshipRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, SponsorshipRepository sponsorshipRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.sponsorshipRepository = sponsorshipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        return convertToDTO(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDTO getEventById(Long id) {
        return eventRepository.findById(id).map(this::convertToDTO).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found for update"));
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setLocation(eventDTO.getLocation());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        return convertToDTO(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO convertToDTO(Event event) {
        return new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getLocation(), event.getStartTime(), event.getEndTime());
    }

    private Event convertToEntity(EventDTO dto) {
        return new Event(dto.getName(), dto.getDescription(), dto.getLocation(), dto.getStartTime(), dto.getEndTime());
    }
    private Optional<User> getCurrentSponsor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username); // Assuming findByUsername returns Optional<User>
        } else {
            String username = principal.toString();
            return userRepository.findByUsername(username);
        }
    }
    @Transactional
    public void sponsorEvent(Long eventId, Double amount) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        Optional<User> sponsorOptional = getCurrentSponsor();
        User sponsor = sponsorOptional.orElseThrow(() -> new RuntimeException("Current sponsor not found"));

        Sponsorship sponsorship = new Sponsorship();
        sponsorship.setEvent(event);
        sponsorship.setSponsor(sponsor);
        sponsorship.setAmount(amount);
        sponsorshipRepository.save(sponsorship);
    }

    public List<Sponsorship> getSponsorshipsBySponsor(Long sponsorId) {
        return sponsorshipRepository.findBySponsorId(sponsorId);
    }
}