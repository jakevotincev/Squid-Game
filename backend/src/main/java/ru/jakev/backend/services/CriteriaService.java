package ru.jakev.backend.services;

import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.entities.Criteria;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface CriteriaService {
    Criteria saveCriteria(CriteriaDTO criteriaDTO);
}
