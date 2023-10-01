package ru.jakev.backend.messages;

import java.security.Principal;
import java.util.List;

/**
 * @author evotintsev
 * @since 30.09.2023
 */
public interface WebSocketMessageSender {
    void sendMessage(String destination, Object message);
    void sendMessage(List<String> destinations, Object message);
    void sendMessageToUser(Principal user, String destination, Object message);


}
