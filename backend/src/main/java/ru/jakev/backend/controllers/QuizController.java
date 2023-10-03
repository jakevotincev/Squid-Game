package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.QuizDTO;
import ru.jakev.backend.services.QuizService;

import java.util.List;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@RestController
@CrossOrigin
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuizDTO>> getQuestions(){
        List<QuizDTO> result = quizService.getQuestionsWithPossibleAnswers(1, 1);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkAnswer/{p_id}/{q_id}/{answer}")
    public ResponseEntity<Boolean> checkAnswer(@PathVariable("p_id") int playerId, @PathVariable("q_id") int id,
                                               @PathVariable("answer") String answer) {
        return ResponseEntity.ok(quizService.checkAnswer(playerId, id, answer.trim()));
    }
}
