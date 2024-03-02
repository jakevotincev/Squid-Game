package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
@Component
public class LunchListener {
    //todo: make configurable from properties?
    //todo: перенести в другое место
    public static final int CORRECT_ANSWER_POINT = 10;
    private final ScoreService scoreService;
    private final GlobalContext globalContext;
    private final WebSocketMessageSender webSocketMessageSender;
    private final Map<Integer, Integer> playerAnsweredQuestions = new ConcurrentHashMap<>();
    private final Set<Integer> answeredAllQuestionsPlayers = ConcurrentHashMap.newKeySet();
    private int readyPlayers = 0;
    private final static Logger LOG = LoggerFactory.getLogger(LunchListener.class);

    public LunchListener(ScoreService scoreService, GlobalContext globalContext,
                         WebSocketMessageSender webSocketMessageSender) {
        this.scoreService = scoreService;
        this.globalContext = globalContext;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    public void userAnswered(int playerId, boolean isCorrect, int questionCount) {
        int answeredQuestions = playerAnsweredQuestions.merge(playerId, 1, Integer::sum);
        if (answeredQuestions > questionCount) {
            return;
        } else if (answeredQuestions == questionCount) {
            LOG.info("Player with id: " + playerId + " answered all questions.");
            answeredAllQuestionsPlayers.add(playerId);
        }

        if (isCorrect) {
            scoreService.addScore(playerId, CORRECT_ANSWER_POINT);
            LOG.info("Player with id: " + playerId + " answered correctly. Added {} point.", CORRECT_ANSWER_POINT);
        } else {
            scoreService.addScore(playerId, 0);
            LOG.info("Player with id: " + playerId + " answered incorrectly.");
        }
    }

    public boolean userReadyToGame(Account user, GamePhase gamePhase, Runnable onAllUsersReady) {
        if (answeredAllQuestionsPlayers.contains(user.getId())) {
            int usersCount;

            if (user.getRole() == Role.PLAYER) {
                usersCount = globalContext.getParticipateInGamePlayers().size();
            } else {
                usersCount = globalContext.getConnectedUsersByCriteria(accountDTO -> accountDTO.getRole() == user.getRole()).size();
            }
            readyPlayers++;
            if (readyPlayers == usersCount) {
                if (gamePhase == GamePhase.LUNCH_MAKING) {
                    notifyLunchIsReady();
                } else if (gamePhase == GamePhase.LUNCH_EATING) {
                    notifyLunchEnd();
                }
                //todo: change phase
                //todo: проверить что с обнулением работает, вроде работает
                onAllUsersReady.run();
                readyPlayers = 0;
                playerAnsweredQuestions.clear();
            }

            answeredAllQuestionsPlayers.remove(user.getId());
            return true;
        } else {
            return false;
        }
    }

    private void notifyLunchIsReady() {
        NotificationMessage message = new NotificationMessage(NotificationMessageType.FOOD_PREPARED);
        webSocketMessageSender.sendMessage(List.of("/glavniy/messages", "/manager/messages"), message);
        globalContext.getParticipateInGamePlayers().keySet().forEach(player -> {
            //todo: проверить эту фичу
            webSocketMessageSender.sendMessageToUser(player, "/player/messages", message);
        });
    }

    private void notifyLunchEnd() {
        NotificationMessage message = new NotificationMessage(NotificationMessageType.LUNCH_END);
        webSocketMessageSender.sendMessage(List.of("/glavniy/messages", "/manager/messages","/player/messages", "/worker/messages"), message);
    }
}
