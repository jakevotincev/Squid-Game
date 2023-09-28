package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.messages.ConfirmMessage;
import ru.jakev.backend.services.CriteriaService;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@RestController
public class GlavniyController {

    private final CriteriaService criteriaService;
    private final FormListener formListener;

    private final static String INTERRUPT_SELECTION_ERROR_MESSAGE = "Error while interrupt form selection";

    public GlavniyController(CriteriaService criteriaService, FormListener formListener) {
        this.criteriaService = criteriaService;
        this.formListener = formListener;
    }

    @MessageMapping("/sendAnswer")
    @SendTo("/manager/messages")
    public ConfirmMessage sendAnswer(ConfirmMessage message){
        if (message.isConfirm()){
            criteriaService.saveCriteria(message.getCriteria());
        }
        return message;
    }

    //todo: переделать все взаимодействия с формами через сервис?
    //todo: + переделать везде нормальные коды респонсов и сообщения
    @GetMapping("/interruptPlayersSelection")
    public ResponseEntity<String> interruptPlayersSelection(){
        //todo: пока нет ситуаций с ошибкой
        boolean stopped = formListener.stopFromSelection();
        return stopped ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(INTERRUPT_SELECTION_ERROR_MESSAGE);
    }

}
