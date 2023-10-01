package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public class NotificationMessage {
    private final NotificationMessageType type;

    public NotificationMessage(NotificationMessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "type=" + type +
                '}';
    }
}
