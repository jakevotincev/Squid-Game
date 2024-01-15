package ru.jakev.backend.services;

import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.entities.Criteria;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface CriteriaService {
    Criteria saveCriteria(CriteriaDTO criteriaDTO);

    Optional<Criteria> getCriteria(long gameId);
}
