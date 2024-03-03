package ru.jakev.backend.entities;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
public enum QuizType {
    GAME_QUIZ("GAME_TYPE"),
    MAKE_FOOD_QUIZ("MAKE_FOOD_QUIZ"),
    EAT_FOOD_QUIZ("EAT_FOOD_QUIZ"),
    PREPARE_GAME_QUIZ("PREPARE_GAME_QUIZ");

    private final String type;

    QuizType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
