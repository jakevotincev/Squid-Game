package ru.jakev.backend.listeners;

import org.springframework.stereotype.Component;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;

import java.util.List;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class FormListener {

    private final WebSocketMessageSender webSocketMessageSender;
    private final GlobalContext globalContext;

    public FormListener(WebSocketMessageSender webSocketMessageSender, GlobalContext globalContext) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.globalContext = globalContext;
    }

    public void allFormsCollected() {
        //todo: add change state
        webSocketMessageSender.sendMessage("/manager/messages",
                new NotificationMessage(NotificationMessageType.ALL_FORMS_COLLECTED));
    }

    public void formAccepted(int playerId) {
        //todo: add change state
        if (globalContext.acceptForm(playerId)) {
            sendFormsSelectionCompletedMessage();
        }
    }

    public boolean stopFormSelection() {
        //todo: add change state + prohibit sending forms
        sendFormsSelectionCompletedMessage();
        return true;
    }

    private void sendFormsSelectionCompletedMessage() {
        NotificationMessage message = new NotificationMessage(NotificationMessageType.FORMS_SELECTION_COMPLETED);
        webSocketMessageSender.sendMessage(List.of("/manager/messages", "/glavniy/messages"), message);
    }
}
