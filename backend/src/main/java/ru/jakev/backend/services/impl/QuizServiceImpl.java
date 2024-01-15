package ru.jakev.backend.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;
import ru.jakev.backend.listeners.GameListener;
import ru.jakev.backend.mappers.QuizMapper;
import ru.jakev.backend.repositories.QuizRepository;
import ru.jakev.backend.services.QuizService;

import java.util.List;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Service
public class QuizServiceImpl implements QuizService {
    private final static Logger LOG = LoggerFactory.getLogger(QuizServiceImpl.class);
    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final GameListener gameListener;

    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper, GameListener gameListener) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.gameListener = gameListener;
    }

    public List<QuizDTO> getQuestionsWithPossibleAnswers(int gameId, int roundId) {
        List<Quiz> quizList = quizRepository.findAllByGameAndRoundId(gameId, roundId);

        return quizList.stream().map(quizMapper::quizToQuizDTO).toList();
    }

    @Override
    public boolean checkAnswer(long playerId, long questionId, String answer) {
        Quiz quiz = quizRepository.findById(questionId).orElse(null);
        int questionCount = quizRepository.countAllByGameIdAndRoundId(1L, 1);
        if (quiz == null) {
            LOG.error("Quiz with id:{} not found", questionId);
            throw new IllegalArgumentException();
        }
        Boolean isCorrect= quiz.getAnswers().get(answer);
        gameListener.playerAnswered(playerId, isCorrect != null && isCorrect, questionCount);

        return isCorrect != null && isCorrect;
    }
}
