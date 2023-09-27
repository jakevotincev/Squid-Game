package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.mappers.CriteriaMapper;
import ru.jakev.backend.repositories.CriteriaRepository;
import ru.jakev.backend.services.CriteriaService;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Service
public class CriteriaServiceImpl implements CriteriaService {
    private final CriteriaRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    public CriteriaServiceImpl(CriteriaRepository criteriaRepository, CriteriaMapper criteriaMapper) {
        this.criteriaRepository = criteriaRepository;
        this.criteriaMapper = criteriaMapper;
    }


    @Override
    public Criteria saveCriteria(CriteriaDTO criteriaDTO) {
        return criteriaRepository.save(criteriaMapper.criteriaDtoToCriteria(criteriaDTO));
    }

    @Override
    public Optional<Criteria> getCriteria(long gameId) {
        return criteriaRepository.findByGameId(gameId);
    }
}
