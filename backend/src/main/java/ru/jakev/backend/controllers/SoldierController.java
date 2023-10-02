package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.listeners.GameListener;
import ru.jakev.backend.messages.KillPlayerMessage;

/**
 * @author evotintsev
 * @since 03.10.2023
 */
@RestController
@CrossOrigin
public class SoldierController {
    private final GameListener gameListener;

    public SoldierController(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    @PostMapping("/killPlayer")
    private ResponseEntity<?> killPlayer(@RequestBody KillPlayerMessage message) {
        boolean success = gameListener.killPlayer(message.getPlayerId(), message.getSoldierId(), message.getScore());
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
