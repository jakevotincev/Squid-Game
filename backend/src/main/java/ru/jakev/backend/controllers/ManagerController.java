package ru.jakev.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.mappers.CriteriaMapper;
import ru.jakev.backend.messages.*;
import ru.jakev.backend.services.CriteriaService;
import ru.jakev.backend.services.FormService;
import ru.jakev.backend.services.GameService;

import java.security.Principal;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@RestController
@CrossOrigin
//todo: сгенерить доку по апи с помощью чего нибудь
public class ManagerController {
    //todo: разделить websocket and rest controllers
    private final Logger LOG = LoggerFactory.getLogger(ManagerController.class);
    private final GameService gameService;
    private final CriteriaService criteriaService;
    private final FormService formService;
    private final CriteriaMapper criteriaMapper;
    private final GlobalContext globalContext;
    private final WebSocketMessageSender webSocketMessageSender;
    private final PhaseManager phaseManager;

    public ManagerController(GameService gameService, CriteriaService criteriaService,
                             FormService formService, CriteriaMapper criteriaMapper, GlobalContext globalContext,
                             WebSocketMessageSender webSocketMessageSender, PhaseManager phaseManager) {
        this.gameService = gameService;
        this.criteriaService = criteriaService;
        this.formService = formService;
        this.criteriaMapper = criteriaMapper;
        this.globalContext = globalContext;
        this.webSocketMessageSender = webSocketMessageSender;
        this.phaseManager = phaseManager;
    }

    //todo: бага если указать больше пользователей в критерии чем будет по факту
    @MessageMapping("/sendCriteriaToGlavniy")
    @SendTo("/glavniy/messages")
    public CriteriaMessage sendCriteriaToGlavniy(CriteriaMessage message) {
        if (phaseManager.isActionNotPermitted(GamePhase.CRITERIA_APPROVAL)) {
            return null;
        }
        //todo: move get game to frontend
        if (message.getCriteria().getGameId() == 0) {
            Game game = gameService.getGame();
            message.getCriteria().setGameId(game.getId());
        }
        return message;
    }


    //user should subscribe to /user/worker/messages path!!!!!!
    @GetMapping("/sendCriteriaAndFormsToWorkers")
    public ResponseEntity<?> sendCriteriaAndFormsToWorkers() {
        //todo: перенести этот код в другое место
        if (!formService.isAllFormsCollected()) {
            LOG.info("Not all forms collected. Cannot send criteria and forms to workers.");
            return ResponseEntity.badRequest().body("Not all forms collected");
        }

        if (phaseManager.isActionNotPermitted(GamePhase.FORMS_SELECTION)) {
            return ResponseEntity.badRequest().body(
                    String.format("Sending forms not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        Set<Principal> workers = globalContext.getConnectedUsersByCriteria(account -> account.getRole() == Role.WORKER);
        List<FormDTO> forms = formService.getAllForms();
        Criteria criteria = criteriaService.getCriteria(1).orElse(null);
        CriteriaDTO criteriaDTO = criteriaMapper.criteriaToCriteriaDto(criteria);
        AtomicInteger playersNumber = new AtomicInteger(criteriaDTO.getPlayersNumber());
        Map<Principal, List<FormDTO>> distributedForms = FormUtils.distributeForms(workers, forms);

        //todo: добавить сохранение количества форм для валидации
        distributedForms.forEach((userPrincipal, formDTOS) -> {
            int acceptedFormsCount = FormUtils.calculateAcceptedFormsCount(criteriaDTO.getPlayersNumber(),
                    workers.size(), playersNumber.get());
            SelectionMessage selectionMessage = new SelectionMessage();
            selectionMessage.setCriteria(criteriaDTO);
            selectionMessage.setForms(formDTOS);
            selectionMessage.setAcceptedFormsCount(acceptedFormsCount);
            playersNumber.addAndGet(-acceptedFormsCount);
            webSocketMessageSender.sendMessageToUser(userPrincipal, "/worker/messages", selectionMessage);
        });

        return ResponseEntity.ok().body("Forms and criteria sent to workers");
    }

    @GetMapping("/startLunch")
    public ResponseEntity<?> startLunch() {
        if (phaseManager.isActionNotPermitted(GamePhase.LUNCH_START_WAITING)) {
            return ResponseEntity.badRequest().body(
                    String.format("Lunch start not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.LUNCH_STARTED);
        //todo: удалить сообщение для плэеров?
        notifyParticipateInGamePlayers(notificationMessage);
        webSocketMessageSender.sendMessage(List.of("/worker/messages", "/glavniy/messages"), notificationMessage);
        //go to LUNCH_MAKING
        phaseManager.startNextPhase();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/startPrepareRound")
    public ResponseEntity<?> startPrepareRound() {
        if (phaseManager.isActionNotPermitted(EnumSet.of(GamePhase.ROUND_PREPARE_AND_TRAINING_WAITING, GamePhase.ROUND_PREPARE_AND_TRAINING))) {
            return ResponseEntity.badRequest().body(
                    String.format("This action not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.START_ROUND_PREPARING);
        webSocketMessageSender.sendMessage(List.of("/worker/messages", "/glavniy/messages"), notificationMessage);
        notifyParticipateInGamePlayers(notificationMessage);

        if (phaseManager.getCurrentPhase() == GamePhase.ROUND_PREPARE_AND_TRAINING_WAITING) {
            //go to ROUND_PREPARE_AND_TRAINING
            phaseManager.startNextPhase();
        }
        return ResponseEntity.ok().build();
    }

    //todo: объеджинить с методом выше
    @GetMapping("/startTraining")
    public ResponseEntity<?> startTraining() {
        if (phaseManager.isActionNotPermitted(EnumSet.of(GamePhase.ROUND_PREPARE_AND_TRAINING_WAITING, GamePhase.ROUND_PREPARE_AND_TRAINING))) {
            return ResponseEntity.badRequest().body(
                    String.format("This action not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.START_TRAINING);
        webSocketMessageSender.sendMessage(List.of("/soldier/messages", "/glavniy/messages"), notificationMessage);
        notifyParticipateInGamePlayers(notificationMessage);

        if (phaseManager.getCurrentPhase() == GamePhase.ROUND_PREPARE_AND_TRAINING_WAITING) {
            //go to ROUND_PREPARE_AND_TRAINING
            phaseManager.startNextPhase();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stopTraining")
    public ResponseEntity<?> stopStaining() {
        if (phaseManager.isActionNotPermitted(GamePhase.ROUND_PREPARE_AND_TRAINING)) {
            return ResponseEntity.badRequest().body(
                    String.format("This action not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.TRAINING_COMPLETED);
        webSocketMessageSender.sendMessage(List.of("/soldier/messages", "/glavniy/messages"), notificationMessage);
        notifyParticipateInGamePlayers(notificationMessage);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/startCleaning")
    public ResponseEntity<?> startCleaning() {
        if (phaseManager.isActionNotPermitted(GamePhase.CLEANING)) {
            return ResponseEntity.badRequest().body(
                    String.format("This action not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.START_CLEANING);
        webSocketMessageSender.sendMessage(List.of("/worker/messages", "/glavniy/messages"), notificationMessage);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stopCleaning")
    public ResponseEntity<?> stopCLeaning() {
        if (phaseManager.isActionNotPermitted(GamePhase.CLEANING)) {
            return ResponseEntity.badRequest().body(
                    String.format("This action not permitted now. Current game phase is %s", phaseManager.getCurrentPhase()));
        }

        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.CLEANING_COMPLETED);
        webSocketMessageSender.sendMessage(List.of("/worker/messages", "/glavniy/messages"), notificationMessage);

        //todo: go to SHOW_RESULTS_WAIT
        phaseManager.startNextPhase();

        return ResponseEntity.ok().build();
    }

    private void notifyParticipateInGamePlayers(NotificationMessage message) {
        globalContext.getParticipateInGamePlayers().forEach((principal, accountDTO) -> {
            webSocketMessageSender.sendMessageToUser(principal, "/player/messages", message);
        });
    }
}
