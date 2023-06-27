package ru.jakev.backend.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.entities.Criteria;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    @Mapping(source = "game.id", target = "gameId")
    CriteriaDTO criteriaToCriteriaDto(Criteria criteria);
    @InheritInverseConfiguration
    Criteria criteriaDtoToCriteria(CriteriaDTO criteriaDTO);
}
