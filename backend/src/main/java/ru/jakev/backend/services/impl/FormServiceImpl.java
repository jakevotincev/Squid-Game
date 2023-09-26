package ru.jakev.backend.services.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Form;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.listeners.Listener;
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
    private final Listener formListener;

    private final GlobalContext globalContext;

    public FormServiceImpl(FormRepository formRepository, FormMapper formMapper,
                           SimpMessagingTemplate simpMessagingTemplate, GlobalContext globalContext) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        formListener = new FormListener(simpMessagingTemplate);
        this.globalContext = globalContext;
    }

    @Override
    public boolean saveForm(FormDTO formDTO) {
        Form form = formMapper.formDtoToform(formDTO);
        //todo: add check account exist

            formRepository.save(form);
            if (isAllFormsCollected()){
                formListener.eventHappened();
            }
            return true;
    }

    @Override
    public List<FormDTO> getAllForms() {
        return formRepository.findAll().stream().map(formMapper::formToFormDto).toList();
    }

    private boolean isAllFormsCollected(){
        long formsCount = formRepository.count();
        return formsCount == globalContext.getPlayersNumber();
    }
}
