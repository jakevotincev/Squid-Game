package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.services.QuizService;

import java.util.EnumSet;
import java.util.List;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@RestController
@CrossOrigin
public class QuizController {

    private final QuizService quizService;
    private final PhaseManager phaseManager;

    private final EnumSet<GamePhase> permittedPhases = EnumSet.of(
            GamePhase.LUNCH_MAKING,
            GamePhase.LUNCH_EATING,
            GamePhase.GAME,
            GamePhase.ROUND_PREPARE_AND_TRAINING);

    public QuizController(QuizService quizService, PhaseManager phaseManager) {
        this.quizService = quizService;
        this.phaseManager = phaseManager;
    }

    @GetMapping("/account/{id}/questions")
    public ResponseEntity<List<QuizDTO>> getQuestions(@PathVariable ("id") int accountId) {
        if (phaseManager.isActionNotPermitted(permittedPhases)) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<QuizDTO> result = quizService.getQuestionsWithPossibleAnswers(accountId, 1, 1, phaseManager.getCurrentPhase());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkAnswer/{p_id}/{q_id}/{answer}")
    public ResponseEntity<Boolean> checkAnswer(@PathVariable("p_id") int playerId, @PathVariable("q_id") int id,
                                               @PathVariable("answer") String answer) {
        if (phaseManager.isActionNotPermitted(permittedPhases)) {
            return ResponseEntity.badRequest().body(false);
        }


        return ResponseEntity.ok(quizService.checkAnswer(playerId, id, answer.trim(), phaseManager.getCurrentPhase()));
    }
}
