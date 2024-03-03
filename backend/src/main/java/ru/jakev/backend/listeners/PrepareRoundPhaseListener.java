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
    private volatile boolean isComplete = false;
    private final WebSocketMessageSender webSocketMessageSender;

    public PrepareRoundPhaseListener(ScoreService scoreService, WebSocketMessageSender webSocketMessageSender,
                                     GlobalContext globalContext) {
        super(scoreService, globalContext);
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @Override
    public void userAnswered(int playerId, boolean isCorrect, int questionCount) {
        if (isComplete) {
            LOG.info("Round preparing is end. Player with id: " + playerId + " can't answer.");
            return;
        }

        super.userAnswered(playerId, isCorrect, questionCount);
    }

    @Override
    protected void notifyAllUsersAnswered() {
        isComplete = true;
        notifyRoundPreparingEnd(List.of("/glavniy/messages", "/manager/messages", "/worker/messages"));
    }

    public boolean interruptRoundPreparing() {
        if (isComplete) {
            return false;
        }

        isComplete = true;
        notifyRoundPreparingEnd(List.of("/worker/messages", "/manager/messages"));
        return true;
    }

    //todo: нужно ли отправлять уведомления о завершении подготовки раунда?
    //todo: убрать воторную отправку когда уже все закончилось, игру можно начать когда главнй получит сообщения о конце тренировок и о конце раунд препаринга
    private void notifyRoundPreparingEnd(List<String> destinations) {
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.ROUND_PREPARING_COMPLETED);
        webSocketMessageSender.sendMessage(destinations, notificationMessage);
    }
}
