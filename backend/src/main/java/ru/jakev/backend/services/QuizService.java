package ru.jakev.backend.services;

import ru.jakev.backend.dto.QuizDTO;

import java.util.List;
import java.util.Map;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public interface QuizService {
    List<QuizDTO> getQuestionsWithPossibleAnswers(int gameId, int roundId);
    boolean checkAnswer(long playerId, long questionId, String answer);
}
