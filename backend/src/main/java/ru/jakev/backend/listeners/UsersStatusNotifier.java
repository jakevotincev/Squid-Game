package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.messages.UsersStatusMessage;
import ru.jakev.backend.messages.WebSocketMessageSender;

import java.util.List;

/**
 * @author evotintsev
 * @since 21.02.2024
 */
@Component
public class UsersStatusNotifier {

    private final WebSocketMessageSender webSocketMessageSender;

    public UsersStatusNotifier(WebSocketMessageSender webSocketMessageSender) {
        this.webSocketMessageSender = webSocketMessageSender;
    }

    //todo: позже добавить рассылку информации о умерших пользователях и т.д.
    public void userStatusChanged(List<AccountDTO> accounts) {
        UsersStatusMessage usersStatusMessage = new UsersStatusMessage(accounts);
        webSocketMessageSender.sendMessage("/glavniy/messages", usersStatusMessage);
    }
}
