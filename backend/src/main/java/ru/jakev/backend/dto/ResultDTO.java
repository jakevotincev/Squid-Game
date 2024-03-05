package ru.jakev.backend.dto;

import ru.jakev.backend.entities.Role;

import java.util.Map;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class ResultDTO {
    private int id;
    private String username;
    private Role role;
    private boolean participatesInGame;
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isParticipatesInGame() {
        return participatesInGame;
    }

    public void setParticipatesInGame(boolean participatesInGame) {
        this.participatesInGame = participatesInGame;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
