package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.EventDTO;
import com.akechsalim.community_service_management_2.model.Event;
import com.akechsalim.community_service_management_2.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        EventDTO dto = new EventDTO(null, "Test Event", "Description", "Location", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Event event = new Event("Test Event", "Description", "Location", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        event.setId(1L);

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO result = eventService.createEvent(dto);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Event");
    }

    @Test
    void testGetAllEvents() {
        Event event1 = new Event("Event1", "Description1", "Location1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        event1.setId(1L);
        Event event2 = new Event("Event2", "Description2", "Location2", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        event2.setId(2L);
        List<Event> events = Arrays.asList(event1, event2);

        when(eventRepository.findAll()).thenReturn(events);

        List<EventDTO> result = eventService.getAllEvents();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Event1");
    }

    @Test
    void testGetEventById() {
        Event event = new Event("Event", "Description", "Location", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        event.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        EventDTO result = eventService.getEventById(1L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Event");
    }

    @Test
    void testUpdateEvent() {
        Event event = new Event("Old Event", "Old Description", "Old Location", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        event.setId(1L);
        EventDTO updatedDTO = new EventDTO(1L, "Updated Event", "Updated Description", "Updated Location", LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(new Event(updatedDTO.getName(), updatedDTO.getDescription(), updatedDTO.getLocation(), updatedDTO.getStartTime(), updatedDTO.getEndTime()));

        EventDTO result = eventService.updateEvent(1L, updatedDTO);
        assertThat(result.getName()).isEqualTo("Updated Event");
    }

    @Test
    void testDeleteEvent() {
        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }
}