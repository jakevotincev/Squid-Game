package ru.jakev.backend.services;

import ru.jakev.backend.dto.FormDTO;

import java.util.List;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface FormService {
    boolean saveForm(FormDTO form);

    //todo: change to get by game id
    List<FormDTO> getAllForms();
    boolean isAllFormsCollected();
}
