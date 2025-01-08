package com.akechsalim.community_service_management_2.dto;

import com.akechsalim.community_service_management_2.model.Role;

import java.util.Objects;

public class UserDTO {
    private Long id;
    private String username;
    private Role role;

    public UserDTO() {
    }

    public UserDTO( Long id,String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}