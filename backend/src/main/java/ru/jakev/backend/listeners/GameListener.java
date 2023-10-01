package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.messages.KillPlayerMessage;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;

import java.security.Principal;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Component
public class GameListener {

    //todo: think about thread safe
    private final Map<Long, Integer> playerIdToAnswersMap = new ConcurrentHashMap<>();
//    private final ArrayBlockingQueue<Long> playersToKillQueue = new ArrayBlockingQueue<>(100);
//    private final AtomicBoolean isNowKilling = new AtomicBoolean(false);

    Queue<Long> playersToKillQueue = new ArrayDeque<>();

    private boolean isKillingNow = false;
    private final WebSocketMessageSender webSocketMessageSender;
    private final GlobalContext globalContext;

    public GameListener(WebSocketMessageSender webSocketMessageSender, GlobalContext globalContext) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.globalContext = globalContext;
    }

    //todo: refactor this method
    public void playerAnswered(long playerId, boolean isCorrect, int questionCount) {
        Principal principal = globalContext.getPrincipalById(playerId);
        //todo: нет защиты от отправки одного и того же ответа
        if (isCorrect) {
            playerIdToAnswersMap.merge(playerId, 1, Integer::sum);
            if (playerIdToAnswersMap.get(playerId) == questionCount) {
                //todo: should connect to /user/player/messages!!!!!!!!!!!!!!!!!
                webSocketMessageSender.sendMessageToUser(principal, "/player/messages",
                        new NotificationMessage(NotificationMessageType.QUALIFIED_TO_NEXT_ROUND_MESSAGE));
            }
        } else {
            playerIdToAnswersMap.remove(playerId);
            //todo: пока не запариваюсь над конкурренси, потом переделать
            //todo: добавить механику убивания
            if (isKillingNow) {
                playersToKillQueue.add(playerId);
            } else {
                isKillingNow = true;
                KillPlayerMessage message = new KillPlayerMessage();
                message.setPlayerId(playerId);
                message.setPlayerName(globalContext.getAccount(principal).getName());
                webSocketMessageSender.sendMessage("/soldier/messages", message);
            }
        }
    }
}
