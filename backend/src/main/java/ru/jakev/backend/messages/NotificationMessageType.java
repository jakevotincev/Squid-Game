package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public enum NotificationMessageType {
    ALL_FORMS_COLLECTED("ALL_FORMS_COLLECTED"),
    CRITERIA_MESSAGE("CRITERIA_MESSAGE"),
    FORMS_SELECTION_COMPLETED("FORMS_SELECTION_COMPLETED"),
    GAME_STARTED("GAME_STARTED"),
    KICKED_FROM_GAME_MESSAGE("KICKED_FROM_GAME_MESSAGE"),
    QUALIFIED_TO_GAME_MESSAGE("QUALIFIED_TO_GAME_MESSAGE"),
    QUALIFIED_TO_NEXT_ROUND_MESSAGE("QUALIFIED_TO_NEXT_ROUND_MESSAGE"),
    PLAYER_KILLED_MESSAGE("PLAYER_KILLED_MESSAGE"),
    MISS_MESSAGE("MISS_MESSAGE"),
    ROLES_DISTRIBUTED("ROLES_DISTRIBUTED"),
    LUNCH_STARTED("LUNCH_STARTED"),
    FOOD_PREPARED("FOOD_PREPARED"),
    LUNCH_END("LUNCH_END"),
    START_ROUND_PREPARING("START_ROUND_PREPARING"),
    ROUND_PREPARING_COMPLETED("ROUND_PREPARING_COMPLETED"),
    START_TRAINING("START_TRAINING"),
    TRAINING_COMPLETED("TRAINING_COMPLETED"),
    GAME_ENDED("GAME_ENDED"),
    START_CLEANING("START_CLEANING"),
    CLEANING_COMPLETED("CLEANING_COMPLETED"),
    RESULTS_READY("RESULTS_READY");

    private final String type;

    NotificationMessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
