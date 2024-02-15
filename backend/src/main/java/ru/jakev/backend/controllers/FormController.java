package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.services.FormService;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@RestController
@CrossOrigin
public class FormController {
    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping("/savePlayerForm")
    public ResponseEntity<?> savePlayerForm(@RequestBody FormDTO form) {
        boolean saved = formService.saveForm(form);
        return saved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
