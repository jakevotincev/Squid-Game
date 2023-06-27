package ru.jakev.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.jakev.backend.messages.CriteriaMessage;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@Controller
@CrossOrigin
public class ManagerController {

    @MessageMapping("/sendCriteria")
    @SendTo("/glavniy/messages")
    public CriteriaMessage sendCriteria(@Payload CriteriaMessage message) {
        return message;
    }
}
