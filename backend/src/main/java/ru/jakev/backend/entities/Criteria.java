package ru.jakev.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Entity
public class Criteria {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @Nonnull
    private Game game;

    private int playersNumber;

    @Nonnull
    private String criteria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    public void setGame(@Nonnull Game game) {
        this.game = game;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    @Nonnull
    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(@Nonnull String criteria) {
        this.criteria = criteria;
    }
}
