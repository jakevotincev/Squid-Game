package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.jakev.backend.entities.Criteria;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
    @Query("select c from Criteria c where c.game.id = :id")
//    Optional<Criteria> findByGameId(Long id);
    Optional<Criteria> findByGameId(@Param("id") Long id);
}
