package ru.jakev.backend.mappers;

import org.springframework.stereotype.Component;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;

import java.util.ArrayList;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Component
public class QuizMapperImpl implements QuizMapper{
    @Override
    public QuizDTO quizToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setQuestion(quiz.getQuestion());
        quizDTO.setAnswers(new ArrayList<>(quiz.getAnswers().keySet()));
        return quizDTO;
    }
}
