package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Form;
import ru.jakev.backend.mappers.FormMapper;
import ru.jakev.backend.repositories.FormRepository;
import ru.jakev.backend.services.FormService;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Service
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final FormMapper formMapper;

    public FormServiceImpl(FormRepository formRepository, FormMapper formMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
    }

    @Override
    public boolean saveForm(FormDTO formDTO) {
        Form form = formMapper.formDtoToform(formDTO);
        //todo: add check account exist
        try {
            formRepository.save(form);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
