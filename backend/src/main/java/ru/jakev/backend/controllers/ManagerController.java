package ru.jakev.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.messages.CriteriaMessage;
import ru.jakev.backend.services.GameService;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@Controller
@CrossOrigin
public class ManagerController {

    private final GameService gameService;

    public ManagerController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/sendCriteria")
    @SendTo("/glavniy/messages")
    public CriteriaMessage sendCriteria(CriteriaMessage message) {
        //todo: move get game to frontend
        if (message.getCriteria().getGameId() == 0){
            Game game = gameService.getGame();
            message.getCriteria().setGameId(game.getId());
        }
        return message;
    }
}
