package ru.jakev.backend.entities;

import jakarta.persistence.*;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Entity
public class Game {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
