package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.game.RoleDistributionManager;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.listeners.PhaseListener;
import ru.jakev.backend.listeners.PrepareRoundPhaseListener;
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
    private final PhaseManager phaseManager;
    private final PrepareRoundPhaseListener prepareRoundPhaseListener;
    private final static String INTERRUPT_SELECTION_ERROR_MESSAGE = "Error while interrupt form selection";
    private final static String GAME_STARTED_MESSAGE = "Game successfully started";
    private final static String INVALID_USERS_NUMBER_MESSAGE = "Invalid number of UNDEFINED users";
    private final static String ROLES_DISTRIBUTION_NOT_PERMITTED_MESSAGE = "Roles distribution is not permitted now. Current game phase is %s";
    private final static String FORMS_COLLECTION_INTERRUPT_NOT_PERMITTED_MESSAGE = "Forms collections interrupt is not permitted now. " +
            "Current game phase is %s";
    private final static String ROUND_PREPARING_INTERRUPT_NOT_PERMITTED_MESSAGE = "Round preparing interrupt is not permitted now. " +
            "Current game phase is %s";
    private final static String INTERRUPT_ROUND_PREPARING_ERROR_MESSAGE = "Round preparing already end";

    public GlavniyController(CriteriaService criteriaService, FormListener formListener,
                             RoleDistributionManager roleDistributionManager, PhaseListener phaseListener,
                             PhaseManager phaseManager, PrepareRoundPhaseListener prepareRoundPhaseListener) {
        this.criteriaService = criteriaService;
        this.formListener = formListener;
        this.roleDistributionManager = roleDistributionManager;
        this.phaseListener = phaseListener;
        this.phaseManager = phaseManager;
        this.prepareRoundPhaseListener = prepareRoundPhaseListener;
    }

    @MessageMapping("/sendAnswer")
    @SendTo("/manager/messages")
    public ConfirmMessage sendAnswer(ConfirmMessage message) {
        if (phaseManager.isActionNotPermitted(GamePhase.CRITERIA_APPROVAL)) {
            return null;
        }

        if (message.isConfirm()) {
            criteriaService.saveCriteria(message.getCriteria());
            // go to FORM_SELECTION
            phaseManager.startNextPhase();
        }
        return message;
    }

    //todo: переделать все взаимодействия с формами через сервис?
    //todo: + переделать везде нормальные коды респонсов и сообщения
    @GetMapping("/interruptPlayersSelection")
    public ResponseEntity<String> interruptPlayersSelection() {
        if (phaseManager.isActionNotPermitted(GamePhase.FORMS_SELECTION)) {
            return ResponseEntity.badRequest().body(String.format(FORMS_COLLECTION_INTERRUPT_NOT_PERMITTED_MESSAGE,
                    phaseManager.getCurrentPhase()));
        }
        //todo: пока нет ситуаций с ошибкой
        boolean stopped = formListener.stopFormSelection();
        return stopped ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(INTERRUPT_SELECTION_ERROR_MESSAGE);
    }

    @GetMapping("/interruptRoundPreparing")
    public ResponseEntity<String> interruptRoundPreparing() {
        if (phaseManager.isActionNotPermitted(GamePhase.ROUND_PREPARE_AND_TRAINING)) {
            return ResponseEntity.badRequest().body(String.format(ROUND_PREPARING_INTERRUPT_NOT_PERMITTED_MESSAGE,
                    phaseManager.getCurrentPhase()));
        }

        boolean isInterrupted = prepareRoundPhaseListener.interruptRoundPreparing();
        return isInterrupted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(INTERRUPT_ROUND_PREPARING_ERROR_MESSAGE);
    }

    @GetMapping("/startGame")
    public ResponseEntity<String> startGame() {
        if (phaseManager.isActionNotPermitted(GamePhase.ROUND_PREPARE_AND_TRAINING)) {
            return ResponseEntity.badRequest().body("Start game is not permitted now. Current game phase is "
                    + phaseManager.getCurrentPhase());
        }

        phaseListener.gameStarted();
        // go to GAME
        phaseManager.startNextPhase();

        return ResponseEntity.ok().body(GAME_STARTED_MESSAGE);
    }

    @GetMapping("/startRolesDistribution")
    public ResponseEntity<String> startRolesDistribution() {
        if (phaseManager.isActionNotPermitted(GamePhase.ROLES_DISTRIBUTION)) {
            return ResponseEntity.badRequest().body(String.format(ROLES_DISTRIBUTION_NOT_PERMITTED_MESSAGE,
                    phaseManager.getCurrentPhase()));
        }

        if (roleDistributionManager.distributeRoles()) {
            phaseListener.rolesDistributed();
            // go to CRITERIA_APPROVAL
            phaseManager.startNextPhase();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(INVALID_USERS_NUMBER_MESSAGE);
        }
    }

    @GetMapping("/showResults")
    public ResponseEntity<String> showResults() {
        if (phaseManager.isActionNotPermitted(GamePhase.SHOW_RESULTS_WAIT)) {
            return ResponseEntity.badRequest().body("Show results is not permitted now. Current game phase is "
                    + phaseManager.getCurrentPhase());
        }

        // go to END
        phaseListener.notifyResultsReady();
        phaseManager.startNextPhase();
        return ResponseEntity.ok().build();
    }
}
