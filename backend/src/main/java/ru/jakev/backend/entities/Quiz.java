package ru.jakev.backend.entities;

import jakarta.persistence.*;

import java.util.Map;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Entity
public class Quiz {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name= "game_id", referencedColumnName = "id", nullable = false)
    private Game game;
    private Integer roundId;
    private String question;
    @Column(columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private Map<String, Boolean> answers;

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

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }
}
