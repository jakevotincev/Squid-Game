package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.services.ScoreService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author evotintsev
 * @since 03.03.2024
 */
public abstract class AbstractQuizListener {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    public static final int CORRECT_ANSWER_POINT = 10;
    private final ScoreService scoreService;
    protected final GlobalContext globalContext;
    protected final Map<Integer, Integer> playerAnsweredQuestions = new ConcurrentHashMap<>();
    protected final Set<Integer> answeredAllQuestionsPlayers = ConcurrentHashMap.newKeySet();
    protected int readyPlayers = 0;

    protected AbstractQuizListener(ScoreService scoreService, GlobalContext globalContext) {
        this.scoreService = scoreService;
        this.globalContext = globalContext;
    }

    public void userAnswered(int playerId, boolean isCorrect, int questionCount) {
        int answeredQuestions = playerAnsweredQuestions.merge(playerId, 1, Integer::sum);
        if (answeredQuestions > questionCount) {
            return;
        } else if (answeredQuestions == questionCount) {
            LOG.info("Player with id: " + playerId + " answered all questions.");
            answeredAllQuestionsPlayers.add(playerId);
            if (isAllUsersAnswered(playerId)){
                notifyAllUsersAnswered();
            }
        }

        if (isCorrect) {
            scoreService.addScore(playerId, CORRECT_ANSWER_POINT);
            LOG.info("Player with id: " + playerId + " answered correctly. Added {} point.", CORRECT_ANSWER_POINT);
        } else {
            scoreService.addScore(playerId, 0);
            LOG.info("Player with id: " + playerId + " answered incorrectly.");
        }
    }

    protected boolean isAllUsersAnswered(int userId) {
        AccountDTO account = globalContext.getAccountById(userId);
        if (account == null) {
            throw new IllegalArgumentException("Account with id: " + userId + " not found.");
        }

        return answeredAllQuestionsPlayers.size() == getQuizParticipantsCount(account.getRole());
    }

    protected abstract void notifyAllUsersAnswered();



    protected int getQuizParticipantsCount(Role role) {
        if (role == Role.PLAYER) {
            return globalContext.getParticipateInGamePlayers().size();
        } else {
            return globalContext.getConnectedUsersByCriteria(accountDTO -> accountDTO.getRole() == role).size();
        }
    }
}
