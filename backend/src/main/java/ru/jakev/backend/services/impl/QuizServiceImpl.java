package ru.jakev.backend.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.entities.Quiz;
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

    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }

    public List<QuizDTO> getQuestionsWithPossibleAnswers(int gameId, int roundId) {
        List<Quiz> quizList = quizRepository.findAllByGameId(gameId, roundId);

        return quizList.stream().map(quizMapper::quizToQuizDTO).toList();
    }

    @Override
    public boolean checkAnswer(long questionId, String answer) {
        Quiz quiz = quizRepository.findById(questionId).orElse(null);
        if (quiz == null) {
            LOG.error("Quiz with id:{} not found", questionId);
            throw new IllegalArgumentException();
        }
        Boolean isCorrect= quiz.getAnswers().get(answer);

        return isCorrect != null && isCorrect;
    }
}
