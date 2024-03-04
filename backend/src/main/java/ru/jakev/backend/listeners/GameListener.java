package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Score;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.messages.KillPlayerMessage;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.ScoreService;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Component
//todo: maybe change name to round listener
public class GameListener {
    private final Logger LOG = LoggerFactory.getLogger(GameListener.class);

    //todo: think about thread safe
    private final Map<Integer, Integer> playerIdToAnswersMap = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> soldierIdToScoreMap = new ConcurrentHashMap<>();
    private Set<Integer> finishGamePlayers = new HashSet<>();
//    private final ArrayBlockingQueue<Long> playersToKillQueue = new ArrayBlockingQueue<>(100);
//    private final AtomicBoolean isNowKilling = new AtomicBoolean(false);

    Queue<Integer> playersToKillQueue = new ArrayDeque<>();
    private boolean isKillingNow = false;
    private final WebSocketMessageSender webSocketMessageSender;
    private final GlobalContext globalContext;
    private final AccountService accountService;
    private final ScoreService scoreService;
    private final PhaseManager phaseManager;
    private final int KILL_BONUS = 50;

    public GameListener(WebSocketMessageSender webSocketMessageSender, GlobalContext globalContext,
                        AccountService accountService, ScoreService scoreService, PhaseManager phaseManager) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.globalContext = globalContext;
        this.accountService = accountService;
        this.scoreService = scoreService;
        this.phaseManager = phaseManager;
    }

    //todo: refactor this method
    public void playerAnswered(int playerId, boolean isCorrect, int questionCount) {
        Principal principal = globalContext.getPrincipalById(playerId);
        //todo: нет защиты от отправки одного и того же ответа
        //todo: добавить переход в междуигровую фазу когда все юзеры сыграли и всех убили
        //todo: add score here?
        if (isCorrect) {
            playerIdToAnswersMap.merge(playerId, 1, Integer::sum);
            if (playerIdToAnswersMap.get(playerId) == questionCount) {
                finishGamePlayers.add(playerId);
                //todo: should connect to /user/player/messages!!!!!!!!!!!!!!!!!
                webSocketMessageSender.sendMessageToUser(principal, "/player/messages",
                        new NotificationMessage(NotificationMessageType.QUALIFIED_TO_NEXT_ROUND_MESSAGE));
                tryToNotifyGameEnded();
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
    public boolean killPlayer(int playerId, int soldierId, int score) {
        int soldierCount = globalContext.getConnectedUsersByCriteria(accountDTO -> accountDTO.getRole() == Role.SOLDIER).size();

        Score trainingScore =  scoreService.getScoreByUserId(soldierId).orElse(null);
        int finalScore = trainingScore == null ? score : score + trainingScore.getScore();

        soldierIdToScoreMap.put(soldierId, finalScore);
        try {
            if (soldierCount == soldierIdToScoreMap.size()) {
                Map.Entry<Integer, Integer> winnerEntry = soldierIdToScoreMap.entrySet().stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue)).orElse(null);
                int winnerId = winnerEntry.getKey();
                soldierIdToScoreMap.remove(winnerId);
                // add bonus for kill
                scoreService.addScore(winnerId, KILL_BONUS);

                Principal soldier = globalContext.getPrincipalById(winnerId);
                Principal killedPlayer = globalContext.getPrincipalById(playerId);
                NotificationMessage message = new NotificationMessage(NotificationMessageType.PLAYER_KILLED_MESSAGE);
                webSocketMessageSender.sendMessageToUser(soldier, "/soldier/messages", message);
                webSocketMessageSender.sendMessageToUser(killedPlayer, "/player/messages", message);
                finishGamePlayers.add(playerId);
                tryToNotifyGameEnded();

                accountService.updateAccountParticipation(playerId, false);
                globalContext.removeParticipateInGamePlayer(killedPlayer);

                globalContext.incrementKilledPlayersCount();


                isKillingNow = false;
                if (!playersToKillQueue.isEmpty()){
                    sendKillMessage(playersToKillQueue.poll());
                }

                sendMissMessages();
                soldierIdToScoreMap.clear();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            Arrays.stream(e.getStackTrace()).forEach(stackTraceElement -> LOG.error(stackTraceElement.toString()));
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

    private void sendKillMessage(int playerId) {
        isKillingNow = true;

        KillPlayerMessage message = new KillPlayerMessage();
        message.setPlayerId(playerId);
        message.setPlayerName(globalContext.getAccount(globalContext.getPrincipalById(playerId)).getUsername());
        webSocketMessageSender.sendMessage("/soldier/messages", message);
    }

    private void tryToNotifyGameEnded() {
        if (globalContext.getParticipateInGamePlayers().size() == finishGamePlayers.size()){
            NotificationMessage message = new NotificationMessage(NotificationMessageType.GAME_ENDED);
            webSocketMessageSender.sendMessage(List.of("/manager/messages", "/glavniy/messages"), message);
            // go to CLEANING
            phaseManager.startNextPhase();
        }
    }
}
