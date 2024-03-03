package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.AccountService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class FormListener {

    private final WebSocketMessageSender webSocketMessageSender;
    private final AccountService accountService;
    private final GlobalContext globalContext;
    private final PhaseManager phaseManager;

    public FormListener(WebSocketMessageSender webSocketMessageSender, AccountService accountService,
                        GlobalContext globalContext, PhaseManager phaseManager) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.accountService = accountService;
        this.globalContext = globalContext;
        this.phaseManager = phaseManager;
    }

    public void allFormsCollected() {
        webSocketMessageSender.sendMessage("/manager/messages",
                new NotificationMessage(NotificationMessageType.ALL_FORMS_COLLECTED));
    }

    public void formAccepted(int playerId) {
        if (globalContext.acceptForm(playerId)) {
            sendFormsSelectionCompletedMessage();
            // go to LUNCH_START_WAITING
            //todo: потенциальная ошибка если главнй прервет селекшон и воркеры доотправят, но по идее на фронте не должно воспроизводиться
            phaseManager.startNextPhase();
        }
    }

    public boolean stopFormSelection() {
        // go to LUNCH_START_WAITING
        phaseManager.startNextPhase();
        sendFormsSelectionCompletedMessage();
        return true;
    }

    //todo: пофиксить нулевое количество игроков при интеррапте
    private void sendFormsSelectionCompletedMessage() {
        Set<Map.Entry<Principal, AccountDTO>> connectedPlayers = globalContext.getConnectedUsers().entrySet().stream()
                .filter(entry -> entry.getValue().getRole() == Role.PLAYER).collect(Collectors.toSet());
        connectedPlayers.forEach(playerEntry -> {
            NotificationMessageType type;
            if (accountService.isAccountParticipatesInGame(playerEntry.getValue().getId())){
                type = NotificationMessageType.QUALIFIED_TO_GAME_MESSAGE;
                globalContext.addParticipateInGamePlayer(playerEntry.getKey(), playerEntry.getValue());
            } else {
                type = NotificationMessageType.KICKED_FROM_GAME_MESSAGE;
            }

            webSocketMessageSender.sendMessageToUser(playerEntry.getKey(), "/player/messages",
                    new NotificationMessage(type));
        });
        NotificationMessage message = new NotificationMessage(NotificationMessageType.FORMS_SELECTION_COMPLETED);
        webSocketMessageSender.sendMessage(List.of("/manager/messages", "/glavniy/messages"), message);
    }
}
