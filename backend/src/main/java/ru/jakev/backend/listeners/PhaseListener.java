package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.messages.*;

import java.util.List;

/**
 * @author evotintsev
 * @since 22.02.2024
 */
@Component
public class PhaseListener {

    private final GlobalContext globalContext;
    private final WebSocketMessageSender webSocketMessageSender;

    public PhaseListener(GlobalContext globalContext, WebSocketMessageSender webSocketMessageSender) {
        this.globalContext = globalContext;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    public void gameStarted() {
        //todo: поменять это на что то получше
        NotificationMessage message = new NotificationMessage(NotificationMessageType.GAME_STARTED);

        globalContext.getParticipateInGamePlayers().forEach(((principal, accountDTO) -> {
            webSocketMessageSender.sendMessageToUser(principal, "/player/messages", message);
        }));
        webSocketMessageSender.sendMessage(List.of("/manager/messages",
                "/soldier/messages",
                "/worker/messages"), message);
    }

    public void rolesDistributed() {
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.ROLES_DISTRIBUTED);
        webSocketMessageSender.sendMessage("/manager/messages", notificationMessage);

        globalContext.getConnectedUsers().forEach(((principal, accountDTO) -> {
            if (accountDTO.getRole() != Role.MANAGER && accountDTO.getRole() != Role.GLAVNIY) {
                RoleAssignedMessage message = new RoleAssignedMessage(MessageType.ROLE_ASSIGNED_MESSAGE,
                        accountDTO.getRole());
                webSocketMessageSender.sendMessageToUser(principal, "/undefined/messages", message);
            }
        }));
    }

    public void notifyResultsReady() {
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.RESULTS_READY);
        webSocketMessageSender.sendMessage(List.of("/manager/messages", "/soldier/messages", "/worker/messages"),
                notificationMessage);

        globalContext.getParticipateInGamePlayers().keySet().forEach(player -> {
            webSocketMessageSender.sendMessageToUser(player, "/player/messages", notificationMessage);
        });
    }
}
