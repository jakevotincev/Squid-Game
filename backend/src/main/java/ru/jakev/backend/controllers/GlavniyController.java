package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.messages.ConfirmMessage;
import ru.jakev.backend.messages.NotificationMessage;
import ru.jakev.backend.messages.NotificationMessageType;
import ru.jakev.backend.messages.WebSocketMessageSender;
import ru.jakev.backend.services.CriteriaService;

import java.util.List;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@RestController
@CrossOrigin
public class GlavniyController {

    private final CriteriaService criteriaService;
    private final FormListener formListener;
    private final WebSocketMessageSender webSocketMessageSender;

    private final static String INTERRUPT_SELECTION_ERROR_MESSAGE = "Error while interrupt form selection";
    private final static String GAME_STARTED_MESSAGE = "Game successfully started";

    public GlavniyController(CriteriaService criteriaService, FormListener formListener,
                             WebSocketMessageSender webSocketMessageSender) {
        this.criteriaService = criteriaService;
        this.formListener = formListener;
        this.webSocketMessageSender = webSocketMessageSender;
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
        NotificationMessage message = new NotificationMessage(NotificationMessageType.GAME_STARTED);
        //todo: поменять это на что то получше
        webSocketMessageSender.sendMessage(List.of("/manager/messages",
                "/player/messages",
                "/soldier/messages",
                "/worker/messages"), message);

        return ResponseEntity.ok().body(GAME_STARTED_MESSAGE);
    }

}
