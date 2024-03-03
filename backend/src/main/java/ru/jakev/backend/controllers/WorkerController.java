package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jakev.backend.dto.ClickerScoreDTO;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.messages.SelectionMessage;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.ScoreService;

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
    private final PhaseManager phaseManager;
    private final ScoreService scoreService;
    private final GlobalContext globalContext;
    private final String SUCCESS_RESPONSE_MESSAGE = "Forms successfully accepted";
    private final String VALIDATION_FAILED_RESPONSE_MESSAGE = "Wrong count of accepted forms";

    public WorkerController(AccountService accountService, FormListener formListener, PhaseManager phaseManager, ScoreService scoreService, GlobalContext globalContext) {
        this.accountService = accountService;
        this.formListener = formListener;
        this.phaseManager = phaseManager;
        this.scoreService = scoreService;
        this.globalContext = globalContext;
    }

    @PostMapping("/acceptForms")
    public ResponseEntity<?> acceptForms(@RequestBody SelectionMessage message) {
        if (phaseManager.isActionNotPermitted(GamePhase.FORMS_SELECTION)) {
            return ResponseEntity.badRequest().body(String.format("Accepting forms not permitted now. Current game phase is %s",
                    phaseManager.getCurrentPhase()));
        }

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

    @PostMapping("/worker/{id}/score")
    private ResponseEntity<?> saveClickerScore(@RequestBody ClickerScoreDTO score, @PathVariable("id") int accountId) {
        //todo: нет защиты от сохранения результата после окончания клининга
        //todo: check worker account
        if (phaseManager.isActionNotPermitted(GamePhase.CLEANING)) {
            return ResponseEntity.badRequest().body("Clicker results saving is not permitted now. Current game phase is "
                    + phaseManager.getCurrentPhase());
        }

        if (globalContext.getKilledPlayersCount() == 0) {
            return ResponseEntity.badRequest().body("Nobody died");
        }

        scoreService.addScore(accountId, score.getScore());

        return ResponseEntity.ok().build();
    }

    //todo: пока валидация условная, фронт может отправить любую херню
    private boolean validateSelectionMessage(SelectionMessage selectionMessage) {
        return selectionMessage.getForms().size() == selectionMessage.getAcceptedFormsCount();
    }
}
