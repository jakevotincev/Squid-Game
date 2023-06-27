package ru.jakev.backend.listeners;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public class FormListener implements Listener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public FormListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void eventHappened() {
        //todo: add change state
        //todo: ошибка при отправке сообщения
        simpMessagingTemplate.convertAndSend("/manager/messages",
                new NotificationMessage(NotificationMessageType.ALL_FORMS_COLLECTED));
    }
}
