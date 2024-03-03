package ru.jakev.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
@Entity
public class Score {
    public Score() {
        score = 0;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Nonnull
    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    @Nonnull
    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    @Nonnull
    private Integer score;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
