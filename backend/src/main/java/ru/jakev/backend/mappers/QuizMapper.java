package ru.jakev.backend.mappers;

import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public interface QuizMapper {
    QuizDTO quizToQuizDTO(Quiz quiz);
}
