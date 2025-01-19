package com.akechsalim.community_service_management_2.dto;

import com.akechsalim.community_service_management_2.model.Role;

import java.util.List;

public class UserWithTasksDTO {
    private Long id;
    private String username;
    private Role role;
    private List<TaskDTO> tasks;

    public UserWithTasksDTO() {
    }
    public UserWithTasksDTO(Long id, String username, Role role, List<TaskDTO> tasks) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}