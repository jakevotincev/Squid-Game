package ru.jakev.backend.services;

import ru.jakev.backend.entities.Score;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
public interface ScoreService {
    void addScore(int userId, int points);
    Optional<Score> getScoreByUserId(int userId);
}
