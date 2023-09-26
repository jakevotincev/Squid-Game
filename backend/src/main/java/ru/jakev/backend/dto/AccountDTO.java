package ru.jakev.backend.dto;

import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
//todo: make immutable
public class AccountDTO {
    private Integer id;
    private String name;
    private Role role;

    public AccountDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
