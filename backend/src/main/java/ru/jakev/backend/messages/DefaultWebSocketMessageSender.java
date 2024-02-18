package ru.jakev.backend.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.listeners.GameListener;

import java.security.Principal;
import java.util.List;

/**
 * @author evotintsev
 * @since 30.09.2023
 */
public class DefaultWebSocketMessageSender implements WebSocketMessageSender {
    private final Logger LOG = LoggerFactory.getLogger(GameListener.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GlobalContext globalContext;

    public DefaultWebSocketMessageSender(SimpMessagingTemplate simpMessagingTemplate, GlobalContext globalContext) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.globalContext = globalContext;
    }

    @Override
    public void sendMessage(String destination, Object message) {
        simpMessagingTemplate.convertAndSend(destination, message);
        LOG.info("Send message {} to {}", message, destination);
    }

    @Override
    public void sendMessage(List<String> destinations, Object message) {
        destinations.forEach(dest -> {
            sendMessage(dest, message);
        });
    }

    @Override
    public void sendMessageToUser(Principal user, String destination, Object message) {
        simpMessagingTemplate.convertAndSendToUser(user.getName(), destination, message);
        LOG.info("Send message {} to {}, user: {}", message, destination, globalContext.getAccount(user).getUsername());
    }
}
