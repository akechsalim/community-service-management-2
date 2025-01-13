package com.akechsalim.community_service_management_2.dto;

import com.akechsalim.community_service_management_2.model.TaskStatus;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long volunteerId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, TaskStatus status, Long volunteerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.volunteerId = volunteerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }
}