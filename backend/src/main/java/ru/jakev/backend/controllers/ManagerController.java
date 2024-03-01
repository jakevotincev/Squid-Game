package ru.jakev.backend.controllers;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.dto.FormDTO;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.mappers.CriteriaMapper;
import ru.jakev.backend.messages.*;
import ru.jakev.backend.services.CriteriaService;
import ru.jakev.backend.services.FormService;
import ru.jakev.backend.services.GameService;

import java.security.Principal;
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

    public ManagerController(GameService gameService, CriteriaService criteriaService,
                             FormService formService, CriteriaMapper criteriaMapper, GlobalContext globalContext,
                             WebSocketMessageSender webSocketMessageSender) {
        this.gameService = gameService;
        this.criteriaService = criteriaService;
        this.formService = formService;
        this.criteriaMapper = criteriaMapper;
        this.globalContext = globalContext;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @MessageMapping("/sendCriteriaToGlavniy")
    @SendTo("/glavniy/messages")
    public CriteriaMessage sendCriteriaToGlavniy(CriteriaMessage message) {
        //todo: move get game to frontend
        if (message.getCriteria().getGameId() == 0) {
            Game game = gameService.getGame();
            message.getCriteria().setGameId(game.getId());
        }
        return message;
    }


    //user should subscribe to /user/worker/messages path!!!!!!
    @GetMapping("/sendCriteriaAndFormsToWorkers")
    public void sendCriteriaAndFormsToWorkers() {
        //todo: перенести этот код в другое место
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
    }

    @GetMapping("/startLunch")
    public ResponseEntity<?> startLunch() {
        //todo: проверка и смена фазы
        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessageType.LUNCH_STARTED);
        webSocketMessageSender.sendMessage(List.of("/worker/messages", "/player/messages", "/glavniy/messages"), notificationMessage);
        return ResponseEntity.ok().build();
    }
}
