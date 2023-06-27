package ru.jakev.backend.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Form;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Mapper(componentModel = "spring")
public interface FormMapper {

    @Mapping(source = "account.id", target = "playerId")
    FormDTO formToFormDto(Form form);

    @InheritInverseConfiguration
    Form formDtoToform(FormDTO formDTO);
}
