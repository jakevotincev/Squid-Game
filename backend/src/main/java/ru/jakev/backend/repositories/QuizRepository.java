package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.jakev.backend.entities.Quiz;

import java.util.List;
import java.util.Optional;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query(value = "select q from Quiz q where q.game.id=:g_id and q.roundId=:r_id")
    List<Quiz> findAllByGameId(@Param("g_id") int gameId, @Param("r_id") int roundId);

    Optional<Quiz> findById(Long id);
}
