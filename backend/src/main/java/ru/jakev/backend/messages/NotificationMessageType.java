package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public enum NotificationMessageType {
    ALL_FORMS_COLLECTED("ALL_FORMS_COLLECTED"),
    FORMS_SELECTION_COMPLETED("FORMS_SELECTION_COMPLETED"),
    GAME_STARTED("GAME_STARTED");

    private final String type;

    NotificationMessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
