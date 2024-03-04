package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.ScoreService;

import java.util.List;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
@Component
public class LunchListener extends AbstractQuizListener {
    //todo: make configurable from properties?
    //todo: перенести в другое место
    private final WebSocketMessageSender webSocketMessageSender;

    protected LunchListener(ScoreService scoreService, GlobalContext globalContext, WebSocketMessageSender webSocketMessageSender) {
        super(scoreService, globalContext);
        this.webSocketMessageSender = webSocketMessageSender;
    }

    public boolean userReadyToGame(Account user, GamePhase gamePhase, Runnable onAllUsersReady) {
        if (answeredAllQuestionsPlayers.contains(user.getId())) {
            int usersCount = getQuizParticipantsCount(user.getRole());

            readyPlayers++;
            if (readyPlayers == usersCount) {
                if (gamePhase == GamePhase.LUNCH_MAKING) {
                    notifyLunchIsReady();
                } else if (gamePhase == GamePhase.LUNCH_EATING) {
                    notifyLunchEnd();
                }
                //todo: проверить что с обнулением работает, вроде работает
                // go to LUNCH_EATING or ROUND_PREPARE_AND_TRAINING_WAITING
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
            webSocketMessageSender.sendMessageToUser(player, "/player/messages", message);
        });
    }

    private void notifyLunchEnd() {
        NotificationMessage message = new NotificationMessage(NotificationMessageType.LUNCH_END);
        webSocketMessageSender.sendMessage(List.of("/glavniy/messages", "/manager/messages", "/worker/messages"), message);
        globalContext.getParticipateInGamePlayers().keySet().forEach(player -> {
            webSocketMessageSender.sendMessageToUser(player, "/player/messages", message);
        });
    }

    @Override
    protected void notifyAllUsersAnswered() {
        //NOOP
    }
}
