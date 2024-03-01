package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.listeners.LunchListener;
import ru.jakev.backend.messages.SelectionMessage;
import ru.jakev.backend.services.AccountService;

import java.util.List;

/**
 * @author evotintsev
 * @since 27.09.2023
 */
@RestController
@CrossOrigin
public class WorkerController {
    private final AccountService accountService;
    private final FormListener formListener;
    private final LunchListener lunchListener;

    private final String SUCCESS_RESPONSE_MESSAGE = "Forms successfully accepted";
    private final String VALIDATION_FAILED_RESPONSE_MESSAGE = "Wrong count of accepted forms";

    public WorkerController(AccountService accountService, FormListener formListener, LunchListener lunchListener) {
        this.accountService = accountService;
        this.formListener = formListener;
        this.lunchListener = lunchListener;
    }

    @PostMapping("/acceptForms")
    public ResponseEntity<?> acceptForms(@RequestBody SelectionMessage message) {
        boolean validated = validateSelectionMessage(message);
        if (validated) {
            List<FormDTO> acceptedForms = message.getForms();
            acceptedForms.forEach(formDTO -> {
                //todo: add logging and move code to right places
                accountService.updateAccountParticipation(formDTO.getPlayerId(), true);
                formListener.formAccepted(formDTO.getPlayerId());
            });
            return ResponseEntity.ok(SUCCESS_RESPONSE_MESSAGE);
        } else {
            return ResponseEntity.badRequest().body(VALIDATION_FAILED_RESPONSE_MESSAGE);
        }
    }

    @GetMapping("/worker/{id}/readyToGame")
    public ResponseEntity<String> readyToGame(@PathVariable("id") int id) {
        boolean ready = lunchListener.userReadyToGame(id);

        return ready ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Player not actually ready");
    }

    //todo: пока валидация условная, фронт может отправить любую херню
    private boolean validateSelectionMessage(SelectionMessage selectionMessage) {
        return selectionMessage.getForms().size() == selectionMessage.getAcceptedFormsCount();
    }
}
