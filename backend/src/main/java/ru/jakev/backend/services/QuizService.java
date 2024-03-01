package ru.jakev.backend.services;

import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.QuizType;

import java.util.List;
import java.util.Map;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public interface QuizService {
    List<QuizDTO> getQuestionsWithPossibleAnswers(int gameId, int roundId, QuizType quizType);
    boolean checkAnswer(int playerId, long questionId, String answer, QuizType quizType);
}
