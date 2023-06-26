package ru.jakev.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.jakev.backend.messages.ConfirmMessage;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@Controller
public class ChiefController {

    @MessageMapping("/sendAnswer")
    @SendTo("/manager/messages")
    public ConfirmMessage sendAnswer(ConfirmMessage message){
        return message;
    }
}
