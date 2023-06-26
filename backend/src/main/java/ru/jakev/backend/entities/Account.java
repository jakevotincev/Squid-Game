package ru.jakev.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author evotintsev
 * @since 25.06.2023
 */
@Entity
//todo: add setters?
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private Role role;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    @Nonnull
    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
