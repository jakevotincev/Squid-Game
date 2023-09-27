package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.listeners.FormListener;
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

    private final String SUCCESS_RESPONSE_MESSAGE = "Forms successfully accepted";
    private final String VALIDATION_FAILED_RESPONSE_MESSAGE = "Wrong count of accepted forms";

    public WorkerController(AccountService accountService, FormListener formListener) {
        this.accountService = accountService;
        this.formListener = formListener;
    }

    @PostMapping("/acceptForms")
    private ResponseEntity<?> acceptForms(@RequestBody SelectionMessage message){
        boolean validated = validateSelectionMessage(message);
        if (validated){
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

    //todo: пока валидация условная, фронт может отправить любую херню
    private boolean validateSelectionMessage(SelectionMessage selectionMessage){
        return selectionMessage.getForms().size() == selectionMessage.getAcceptedFormsCount();
    }
}
