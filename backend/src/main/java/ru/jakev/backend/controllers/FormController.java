package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.services.FormService;

import java.util.EnumSet;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@RestController
@CrossOrigin
public class FormController {
    private final FormService formService;
    private final PhaseManager phaseManager;

    private final String SENDING_FORMS_NOT_PERMITTED = "Sending forms not permitted now. Current game phase is %s";

    public FormController(FormService formService, PhaseManager phaseManager) {
        this.formService = formService;
        this.phaseManager = phaseManager;
    }

    @PostMapping("/savePlayerForm")
    public ResponseEntity<?> savePlayerForm(@RequestBody FormDTO form) {
        if (phaseManager.isActionNotPermitted(EnumSet.of(GamePhase.CRITERIA_APPROVAL ,GamePhase.FORMS_SELECTION ))) {
           return ResponseEntity.badRequest().body(String.format(SENDING_FORMS_NOT_PERMITTED, phaseManager.getCurrentPhase()));
        }

        boolean saved = formService.saveForm(form);
        return saved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
