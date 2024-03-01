package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jakev.backend.entities.Score;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByAccountId(Integer id);
}
