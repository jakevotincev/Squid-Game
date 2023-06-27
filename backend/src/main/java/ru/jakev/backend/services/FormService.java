package ru.jakev.backend.services;

import ru.jakev.backend.dto.FormDTO;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface FormService {
    boolean saveForm(FormDTO form);
}
