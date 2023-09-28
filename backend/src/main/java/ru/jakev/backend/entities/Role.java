package ru.jakev.backend.entities;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
public enum Role {
    GLAVNIY("GLAVNIY"), MANAGER("MANAGER"), PLAYER("PLAYER"), SOLDIER("SOLDIER"), WORKER("WORKER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
