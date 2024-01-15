package ru.jakev.backend.dto;

import java.util.List;
import java.util.Map;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public class QuizDTO {
    private Long id;
    private String question;
    private List<String> answers;

    public QuizDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuizDTO{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                '}';
    }
}
