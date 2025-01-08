package com.akechsalim.community_service_management_2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventDTO {
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public EventDTO() {
    }

    public EventDTO( Long id ,String name, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(id, eventDTO.id) && Objects.equals(name, eventDTO.name) && Objects.equals(description, eventDTO.description) && Objects.equals(location, eventDTO.location) && Objects.equals(startTime, eventDTO.startTime) && Objects.equals(endTime, eventDTO.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location, startTime, endTime);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}