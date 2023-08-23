package org.example.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    MODERATOR,
    USER;


    @Override
    public String getAuthority() {
        return name();
    }
}
