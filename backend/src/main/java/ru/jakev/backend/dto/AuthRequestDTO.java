package ru.jakev.backend.dto;

/**
 * @author evotintsev
 * @since 18.02.2024
 */
public class AuthRequestDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
