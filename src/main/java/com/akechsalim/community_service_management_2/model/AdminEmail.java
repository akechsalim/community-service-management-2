package com.akechsalim.community_service_management_2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AdminEmail {
    @Id
    private String email;

    public AdminEmail() {
    }

    public AdminEmail(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}