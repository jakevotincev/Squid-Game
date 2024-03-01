package ru.jakev.backend.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;
import ru.jakev.backend.entities.QuizType;
import ru.jakev.backend.listeners.GameListener;
import ru.jakev.backend.listeners.LunchListener;
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

    private final LunchListener lunchListener;

    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper, GameListener gameListener,
                           LunchListener lunchListener) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.gameListener = gameListener;
        this.lunchListener = lunchListener;
    }

    public List<QuizDTO> getQuestionsWithPossibleAnswers(int gameId, int roundId, QuizType quizType) {
        List<Quiz> quizList = quizRepository.findAllByGameAndRoundIdAndQuizType(gameId, roundId, quizType);

        return quizList.stream().map(quizMapper::quizToQuizDTO).toList();
    }

    @Override
    public boolean checkAnswer(int userId, long questionId, String answer, QuizType quizType) {
        Quiz quiz = quizRepository.findById(questionId).orElse(null);
        int questionCount = quizRepository.countAllByGameIdAndRoundIdAndQuizType(1L, 1, quizType);
        if (quiz == null) {
            LOG.error("Quiz with id:{} not found", questionId);
            throw new IllegalArgumentException();
        }
        Boolean isCorrect = quiz.getAnswers().get(answer);

        //todo: подумать как объединить листенеры
        if (quizType == QuizType.GAME_QUIZ) {
            gameListener.playerAnswered(userId, isCorrect != null && isCorrect, questionCount);
        } else {
            lunchListener.userAnswered(userId, isCorrect != null && isCorrect, questionCount);
        }


        return isCorrect != null && isCorrect;
    }
}
