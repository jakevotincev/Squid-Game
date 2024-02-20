package ru.jakev.backend.dto;

import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
//todo: make immutable
public class AccountDTO {
    private Integer id;
    private String username;
    private String password;
    private Role role;
    private Boolean participatesInGame;

    public AccountDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getParticipatesInGame() {
        return participatesInGame;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setParticipatesInGame(Boolean participatesInGame) {
        this.participatesInGame = participatesInGame;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
