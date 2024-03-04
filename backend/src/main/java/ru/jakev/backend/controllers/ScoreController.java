package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.dto.ResultDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.services.ScoreService;

import java.util.Map;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
@RestController
@CrossOrigin
public class ScoreController {
    private final ScoreService scoreService;
    private final PhaseManager phaseManager;

    public ScoreController(ScoreService scoreService, PhaseManager phaseManager) {
        this.scoreService = scoreService;
        this.phaseManager = phaseManager;
    }

    @GetMapping("/account/{id}/results")
    public ResponseEntity<?> results(@PathVariable("id") Integer id) {
        if (phaseManager.isActionNotPermitted(GamePhase.END)) {
            return ResponseEntity.badRequest().body("Game is not finished yet");
        }

        Map<AccountDTO, Integer> results = scoreService.getResultsForUser(id);
        ResultDTO resultDTO = new ResultDTO(results);
        return ResponseEntity.ok().body(resultDTO);

    }
}
