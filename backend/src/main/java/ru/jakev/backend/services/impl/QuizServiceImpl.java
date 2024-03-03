package ru.jakev.backend.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;
import ru.jakev.backend.entities.QuizType;
import ru.jakev.backend.entities.Score;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.listeners.GameListener;
import ru.jakev.backend.listeners.LunchListener;
import ru.jakev.backend.listeners.PrepareRoundPhaseListener;
import ru.jakev.backend.mappers.QuizMapper;
import ru.jakev.backend.repositories.QuizRepository;
import ru.jakev.backend.services.QuizService;
import ru.jakev.backend.services.ScoreService;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private final PrepareRoundPhaseListener prepareRoundPhaseListener;
    private final ScoreService scoreService;

    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper, GameListener gameListener,
                           LunchListener lunchListener, PrepareRoundPhaseListener prepareRoundPhaseListener, ScoreService scoreService) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.gameListener = gameListener;
        this.lunchListener = lunchListener;
        this.prepareRoundPhaseListener = prepareRoundPhaseListener;
        this.scoreService = scoreService;
    }

    public List<QuizDTO> getQuestionsWithPossibleAnswers(int accountId, int gameId, int roundId, GamePhase phase) {
        QuizType quizType = getQuizTypeByPhase(phase);
        List<Quiz> quizList = quizRepository.findAllByGameAndRoundIdAndQuizType(gameId, roundId, quizType);

        if (quizType == QuizType.GAME_QUIZ) {
            correctAnswersForUser(quizList, accountId);
        } else if (quizType == QuizType.PREPARE_GAME_QUIZ) {
            //todo: add change to empty quiz?
            removeAllAnswers(quizList);
        }

        return quizList.stream().map(quizMapper::quizToQuizDTO).toList();
    }

    @Override
    public boolean checkAnswer(int userId, long questionId, String answer, GamePhase phase) {
        QuizType quizType = getQuizTypeByPhase(phase);
        Quiz quiz = quizRepository.findById(questionId).orElse(null);
        int questionCount = quizRepository.countAllByGameIdAndRoundIdAndQuizType(1L, 1, quizType);

        if (quiz == null) {
            LOG.error("Quiz with id:{} not found", questionId);
            throw new IllegalArgumentException();
        }

        Boolean isCorrect = quiz.getAnswers().get(answer);

        //todo: подумать как объединить листенеры
        switch (quizType) {
            case MAKE_FOOD_QUIZ, EAT_FOOD_QUIZ -> lunchListener.userAnswered(userId, isCorrect != null && isCorrect, questionCount);
            case PREPARE_GAME_QUIZ -> prepareRoundPhaseListener.userAnswered(userId, isCorrect != null && isCorrect, questionCount);
            case GAME_QUIZ -> gameListener.playerAnswered(userId, isCorrect != null && isCorrect, questionCount);
        }

        return isCorrect != null && isCorrect;
    }

    private void correctAnswersForUser(List<Quiz> quizList, int accountId) {
        Score score = scoreService.getScoreByUserId(accountId).orElse(null);
        if (score == null) {
            //todo: поменять тип исключения
            throw new IllegalArgumentException("Score for user with id=" + accountId + " not found");
        }


        int deleteQuestionsCount = score.getScore() / LunchListener.CORRECT_ANSWER_POINT;
        if (deleteQuestionsCount > 0) {
            quizList.forEach(quiz -> {
                //todo: подумать как менять порядок элементов
                removeAnswers(quiz.getAnswers(), deleteQuestionsCount, true);
            });
        }
    }

    private void removeAnswers(Map<String, Boolean> answers, int deleteQuestionsCount, boolean onlyIncorrectAnswers) {
        Iterator<Map.Entry<String, Boolean>> iter = answers.entrySet().iterator();
        int deleteCount = 0;

        while (iter.hasNext() && deleteCount < deleteQuestionsCount) {
            Map.Entry<String, Boolean> entry = iter.next();
            if (!onlyIncorrectAnswers || entry.getValue()) {
                iter.remove();
                deleteCount++;
            }
        }
    }

    private void removeAllAnswers(List<Quiz> quizList) {
        Quiz firstQuiz = quizList.get(0);
        if (firstQuiz == null) {
            throw new IllegalArgumentException("Quiz list is empty");
        }

        int answersCount = firstQuiz.getAnswers().size();
        quizList.forEach(quiz -> {
            //todo: подумать как менять порядок элементов
            removeAnswers(quiz.getAnswers(), answersCount, false);
        });
    }

    private QuizType getQuizTypeByPhase(GamePhase phase) {
        return switch (phase) {
            case LUNCH_MAKING -> QuizType.MAKE_FOOD_QUIZ;
            case LUNCH_EATING -> QuizType.EAT_FOOD_QUIZ;
            case GAME -> QuizType.GAME_QUIZ;
            case ROUND_PREPARE_AND_TRAINING -> QuizType.PREPARE_GAME_QUIZ;
            default -> throw new IllegalArgumentException("Unknown phase: " + phase);
        };
    }
}
