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

    @OneToOne(mappedBy = "game")
    private Criteria criteria;

    public Game() {
    }

    public Game(Long id, Criteria criteria) {
        this.id = id;
        this.criteria = criteria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
}
