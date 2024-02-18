package ru.jakev.backend.dto;

/**
 * @author evotintsev
 * @since 18.02.2024
 */

public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
