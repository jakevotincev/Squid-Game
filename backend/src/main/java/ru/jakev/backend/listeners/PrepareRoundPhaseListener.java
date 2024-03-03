package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.ScoreService;

import java.util.List;

/**
 * @author evotintsev
 * @since 03.03.2024
 */
@Component
public class PrepareRoundPhaseListener extends AbstractQuizListener {
    private final static Logger LOG = LoggerFactory.getLogger(PrepareRoundPhaseListener.class);
    private volatile boolean isInterrupted = false;
    private final WebSocketMessageSender webSocketMessageSender;

    public PrepareRoundPhaseListener(ScoreService scoreService, WebSocketMessageSender webSocketMessageSender,
                                     GlobalContext globalContext) {
        super(scoreService, globalContext);
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @Override
    public void userAnswered(int playerId, boolean isCorrect, int questionCount) {
        if (isInterrupted) {
            LOG.info("Round preparing is interrupted. Player with id: " + playerId + " can't answer.");
            return;
        }

        super.userAnswered(playerId, isCorrect, questionCount);
    }

    @Override
    protected void notifyAllUsersAnswered() {
        notifyRoundPreparingEnd(List.of("/glavniy/messages", "/manager/messages"));
    }

    public boolean interruptRoundPreparing() {
        //todo: add notify
        isInterrupted = true;
        notifyRoundPreparingEnd(List.of("/worker/messages", "/manager/messages"));
        return isInterrupted;
    }

    //todo: нужно ли отправлять уведомления о завершении подготовки раунда?
    private void notifyRoundPreparingEnd(List<String> destinations) {
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.ROUND_PREPARING_COMPLETED);
        webSocketMessageSender.sendMessage(destinations, notificationMessage);
    }
}
