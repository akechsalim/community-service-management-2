package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.EventDTO;
import com.akechsalim.community_service_management_2.model.Event;
import com.akechsalim.community_service_management_2.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}