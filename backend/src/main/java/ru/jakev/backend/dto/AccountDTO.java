package ru.jakev.backend.dto;

import ru.jakev.backend.entities.Role;

import java.util.Objects;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
//todo: make immutable
public class AccountDTO {
    private Integer id;
    private String username;
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

    public Role getRole() {
        return role;
    }

    public Boolean getParticipatesInGame() {
        return participatesInGame;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setParticipatesInGame(Boolean participatesInGame) {
        this.participatesInGame = participatesInGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", participatesInGame=" + participatesInGame +
                '}';
    }
}
