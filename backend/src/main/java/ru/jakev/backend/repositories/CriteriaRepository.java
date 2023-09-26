package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jakev.backend.entities.Criteria;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface CriteriaRepository extends JpaRepository<Criteria, Long> {

}
