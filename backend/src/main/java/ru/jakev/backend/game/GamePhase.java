package ru.jakev.backend.game;

/**
 * @author evotintsev
 * @since 02.03.2024
 */
public enum GamePhase {
    ROLES_DISTRIBUTION("ROLES_DISTRIBUTION"),
    CRITERIA_APPROVAL("CRITERIA_APPROVAL"),
    FORMS_SELECTION("FORMS_SELECTION"),
    LUNCH_START_WAITING("LUNCH_START_WAITING"),
    LUNCH_MAKING("LUNCH_MAKING"),
    LUNCH_EATING("LUNCH_EATING"),
    GAME("GAME"),

    END("END");

    private final String phase;

    GamePhase(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }
}
