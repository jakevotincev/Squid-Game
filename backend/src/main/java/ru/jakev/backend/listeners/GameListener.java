package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.messages.KillPlayerMessage;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.AccountService;

import java.security.Principal;
import java.util.ArrayDeque;
import java.util.Comparator;
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
    private final Map<Long, Integer> soldierIdToScoreMap = new ConcurrentHashMap<>();
//    private final ArrayBlockingQueue<Long> playersToKillQueue = new ArrayBlockingQueue<>(100);
//    private final AtomicBoolean isNowKilling = new AtomicBoolean(false);

    Queue<Long> playersToKillQueue = new ArrayDeque<>();
    private boolean isKillingNow = false;
    private final WebSocketMessageSender webSocketMessageSender;
    private final GlobalContext globalContext;
    private final AccountService accountService;

    public GameListener(WebSocketMessageSender webSocketMessageSender, GlobalContext globalContext, AccountService accountService) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.globalContext = globalContext;
        this.accountService = accountService;
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
            if (isKillingNow) {
                playersToKillQueue.add(playerId);
            } else {
                sendKillMessage(playerId);
            }
        }
    }

    //todo: refactor
    //todo: change all message path to constant
    //todo: remove playerId from here?
    public boolean killPlayer(long playerId, long soldierId, int score) {
        int soldierCount = globalContext.getConnectedUsersByCriteria(accountDTO -> accountDTO.getRole() == Role.SOLDIER).size();
        soldierIdToScoreMap.put(soldierId, score);
        try {
            if (soldierCount == soldierIdToScoreMap.size()) {
                Map.Entry<Long, Integer> winnerEntry = soldierIdToScoreMap.entrySet().stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue)).orElse(null);
                Long winnerId = winnerEntry.getKey();
                soldierIdToScoreMap.remove(winnerId);

                Principal soldier = globalContext.getPrincipalById(winnerId);
                Principal killedPlayer = globalContext.getPrincipalById(playerId);
                NotificationMessage message = new NotificationMessage(NotificationMessageType.PLAYER_KILLED_MESSAGE);
                webSocketMessageSender.sendMessageToUser(soldier, "/soldier/messages", message);
                webSocketMessageSender.sendMessageToUser(killedPlayer, "/player/messages", message);
                accountService.updateAccountParticipation((int) playerId, false);


                isKillingNow = false;
                if (!playersToKillQueue.isEmpty()){
                    sendKillMessage(playersToKillQueue.poll());
                }

                sendMissMessages();
                soldierIdToScoreMap.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendMissMessages() {
        soldierIdToScoreMap.keySet().forEach(key -> {
            webSocketMessageSender.sendMessageToUser(globalContext.getPrincipalById(key), "/soldier/messages",
                    new NotificationMessage(NotificationMessageType.MISS_MESSAGE));
        });
    }

    private void sendKillMessage(long playerId) {
        isKillingNow = true;

        KillPlayerMessage message = new KillPlayerMessage();
        message.setPlayerId(playerId);
        message.setPlayerName(globalContext.getAccount(globalContext.getPrincipalById(playerId)).getName());
        webSocketMessageSender.sendMessage("/soldier/messages", message);
    }
}
