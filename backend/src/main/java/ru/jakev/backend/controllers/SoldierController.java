package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jakev.backend.dto.ClickerScoreDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.listeners.GameListener;
import ru.jakev.backend.messages.KillPlayerMessage;
import ru.jakev.backend.services.ScoreService;

/**
 * @author evotintsev
 * @since 03.10.2023
 */
@RestController
@CrossOrigin
public class SoldierController {
    private final GameListener gameListener;
    private final ScoreService scoreService;
    private final PhaseManager phaseManager;

    public SoldierController(GameListener gameListener, ScoreService scoreService, PhaseManager phaseManager) {
        this.gameListener = gameListener;
        this.scoreService = scoreService;
        this.phaseManager = phaseManager;
    }

    @PostMapping("/killPlayer")
    private ResponseEntity<?> killPlayer(@RequestBody KillPlayerMessage message) {
        if (phaseManager.isActionNotPermitted(GamePhase.GAME)) {
            return ResponseEntity.badRequest().body("Killing player is not permitted now. Current game phase is "
                    + phaseManager.getCurrentPhase());
        }

        //todo: add phase check
        boolean success = gameListener.killPlayer(message.getPlayerId(), message.getSoldierId(), message.getScore());
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/soldier/{id}/score")
    private ResponseEntity<?> saveClickerScore(@RequestBody ClickerScoreDTO score, @PathVariable("id") int accountId) {
        //todo: нет защиты от сохранения результата после окончания тренировки
        //todo: check soldier account
        if (phaseManager.isActionNotPermitted(GamePhase.ROUND_PREPARE_AND_TRAINING)) {
            return ResponseEntity.badRequest().body("Clicker results saving is not permitted now. Current game phase is "
                    + phaseManager.getCurrentPhase());
        }

        scoreService.addScore(accountId, score.getScore());

        return ResponseEntity.ok().build();
    }
}
