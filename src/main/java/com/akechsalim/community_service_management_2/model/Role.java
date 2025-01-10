package com.akechsalim.community_service_management_2.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    VOLUNTEER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name(); // Prefixes the enum name with "ROLE_"
    }
}