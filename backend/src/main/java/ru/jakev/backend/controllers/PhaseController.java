package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jakev.backend.dto.PhaseDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;

/**
 * @author evotintsev
 * @since 02.03.2024
 */
@RestController
@RequestMapping("/phase")
public class PhaseController {
    private final PhaseManager phaseManager;

    public PhaseController(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
    }

    //todo: для тестов
    @PostMapping("/setPhase")
    public ResponseEntity<?> changePhase(@RequestBody PhaseDTO phaseDTO) {
        GamePhase phase = GamePhase.valueOf(phaseDTO.getPhaseName());
        phaseManager.changePhase(phase);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PhaseDTO> getPhase() {
        PhaseDTO phaseDTO = new PhaseDTO();
        phaseDTO.setPhaseName(phaseManager.getCurrentPhase().name());
        return ResponseEntity.ok(phaseDTO);
    }
}
