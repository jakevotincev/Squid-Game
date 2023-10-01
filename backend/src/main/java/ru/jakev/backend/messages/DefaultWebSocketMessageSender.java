package ru.jakev.backend.messages;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

/**
 * @author evotintsev
 * @since 30.09.2023
 */
public class DefaultWebSocketMessageSender implements WebSocketMessageSender {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public DefaultWebSocketMessageSender(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendMessage(String destination, Object message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

    @Override
    public void sendMessage(List<String> destinations, Object message) {
        destinations.forEach(dest -> {
            simpMessagingTemplate.convertAndSend(dest, message);
        });
    }

    @Override
    public void sendMessageToUser(String username, String destination, Object message) {
        simpMessagingTemplate.convertAndSendToUser(username, destination, message);
    }
}
