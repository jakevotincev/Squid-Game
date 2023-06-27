package ru.jakev.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.jakev.backend.messages.ConfirmMessage;
import ru.jakev.backend.services.CriteriaService;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@Controller
public class GlavniyController {

    private final CriteriaService criteriaService;

    public GlavniyController(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    @MessageMapping("/sendAnswer")
    @SendTo("/manager/messages")
    public ConfirmMessage sendAnswer(ConfirmMessage message){
        if (message.isConfirm()){
            criteriaService.saveCriteria(message.getCriteria());
        }
        return message;
    }
}
