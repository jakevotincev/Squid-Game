package ru.jakev.backend.entities;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
public enum Role implements GrantedAuthority {
    GLAVNIY("GLAVNIY"),
    MANAGER("MANAGER"),
    PLAYER("PLAYER"),
    SOLDIER("SOLDIER"),
    WORKER("WORKER"),
    UNDEFINED("UNDEFINED");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
