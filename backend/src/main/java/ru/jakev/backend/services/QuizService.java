package ru.jakev.backend.services;

import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.game.GamePhase;

import java.util.List;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public interface QuizService {
    List<QuizDTO> getQuestionsWithPossibleAnswers(int accountId, int gameId, int roundId, GamePhase phase);
    boolean checkAnswer(int playerId, long questionId, String answer, GamePhase phase);
}
