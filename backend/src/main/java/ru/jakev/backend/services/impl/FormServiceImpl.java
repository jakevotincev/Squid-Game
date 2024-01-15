package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Form;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.mappers.FormMapper;
import ru.jakev.backend.repositories.FormRepository;
import ru.jakev.backend.services.FormService;

import java.util.List;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Service
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final FormMapper formMapper;
    private final FormListener formListener;

    private final GlobalContext globalContext;

    public FormServiceImpl(FormRepository formRepository, FormMapper formMapper,
                           FormListener formListener, GlobalContext globalContext) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        this.formListener = formListener;
        this.globalContext = globalContext;
    }

    @Override
    public boolean saveForm(FormDTO formDTO) {
        Form form = formMapper.formDtoToform(formDTO);
        //todo: add check account exist

            formRepository.save(form);
            if (isAllFormsCollected()){
                formListener.allFormsCollected();
            }
            return true;
    }

    @Override
    public List<FormDTO> getAllForms() {
        return formRepository.findAll().stream().map(formMapper::formToFormDto).toList();
    }

    private boolean isAllFormsCollected(){
        //todo: исправить юзкейс если какие то типы отправят формы, потом ливнут и формы других игроков посчитаются за других
        long formsCount = formRepository.count();
        return formsCount == globalContext.getConnectedPlayersCount();
    }
}
