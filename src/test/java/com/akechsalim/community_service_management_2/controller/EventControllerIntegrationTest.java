package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createEventTest() throws Exception {
        EventDTO eventDTO = new EventDTO(null, "Test Event", "Test", "Location", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void getAllEventsTest() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getEventByIdTest() throws Exception {
        // Assuming an event with id 1 exists
        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateEventTest() throws Exception {
        EventDTO eventDTO = new EventDTO(1L, "Updated Event", "Updated", "Updated Location", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Event"));
    }

    @Test
    public void deleteEventTest() throws Exception {
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }
}