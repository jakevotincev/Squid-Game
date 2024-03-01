package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.game.RoleDistributionManager;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.listeners.PhaseListener;
import ru.jakev.backend.messages.ConfirmMessage;
import ru.jakev.backend.services.CriteriaService;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@RestController
@CrossOrigin
public class GlavniyController {

    private final CriteriaService criteriaService;
    private final FormListener formListener;
    private final RoleDistributionManager roleDistributionManager;
    private final PhaseListener phaseListener;
    private final static String INTERRUPT_SELECTION_ERROR_MESSAGE = "Error while interrupt form selection";
    private final static String GAME_STARTED_MESSAGE = "Game successfully started";
    private final static String INVALID_USERS_NUMBER_MESSAGE = "Invalid number of UNDEFINED users";

    public GlavniyController(CriteriaService criteriaService, FormListener formListener,
                             RoleDistributionManager roleDistributionManager, PhaseListener phaseListener) {
        this.criteriaService = criteriaService;
        this.formListener = formListener;
        this.roleDistributionManager = roleDistributionManager;
        this.phaseListener = phaseListener;
    }

    @MessageMapping("/sendAnswer")
    @SendTo("/manager/messages")
    public ConfirmMessage sendAnswer(ConfirmMessage message) {
        if (message.isConfirm()) {
            criteriaService.saveCriteria(message.getCriteria());
        }
        return message;
    }

    //todo: переделать все взаимодействия с формами через сервис?
    //todo: + переделать везде нормальные коды респонсов и сообщения
    @GetMapping("/interruptPlayersSelection")
    public ResponseEntity<String> interruptPlayersSelection() {
        //todo: пока нет ситуаций с ошибкой
        boolean stopped = formListener.stopFormSelection();
        return stopped ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(INTERRUPT_SELECTION_ERROR_MESSAGE);
    }

    @GetMapping("/startGame")
    public ResponseEntity<String> startGame() {
        //todo: add check and change phase
        phaseListener.gameStarted();

        return ResponseEntity.ok().body(GAME_STARTED_MESSAGE);
    }

    @GetMapping("/startRolesDistribution")
    public ResponseEntity<String> startRolesDistribution() {
        if (roleDistributionManager.distributeRoles()) {
            phaseListener.rolesDistributed();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(INVALID_USERS_NUMBER_MESSAGE);
        }
    }
}
