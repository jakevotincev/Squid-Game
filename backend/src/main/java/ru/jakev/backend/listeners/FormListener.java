package ru.jakev.backend.listeners;

import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class FormListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GlobalContext globalContext;

    public FormListener(SimpMessagingTemplate simpMessagingTemplate, GlobalContext globalContext) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.globalContext = globalContext;
        simpMessagingTemplate.setMessageConverter(new GsonMessageConverter());
    }

    public void allFormsCollected() {
        //todo: add change state
        simpMessagingTemplate.convertAndSend("/manager/messages",
                new NotificationMessage(NotificationMessageType.ALL_FORMS_COLLECTED));
    }

    public void formAccepted(int playerId) {
        //todo: add change state
        if (globalContext.acceptForm(playerId)){
            sendFormsSelectionCompletedMessage();
        }
    }

    public boolean stopFromSelection() {
        //todo: add change state + prohibit sending forms
        sendFormsSelectionCompletedMessage();
        return true;
    }

    private void sendFormsSelectionCompletedMessage(){
        NotificationMessage message = new NotificationMessage(NotificationMessageType.FORMS_SELECTION_COMPLETED);
        simpMessagingTemplate.convertAndSend("/manager/messages", message);
        simpMessagingTemplate.convertAndSend("/glavniy/messages", message);
    }
}
