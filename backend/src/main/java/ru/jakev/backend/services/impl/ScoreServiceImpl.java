package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.entities.Score;
import ru.jakev.backend.repositories.ScoreRepository;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.GameService;
import ru.jakev.backend.services.ScoreService;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final AccountService accountService;

    private final GameService gameService;

    public ScoreServiceImpl(ScoreRepository scoreRepository, AccountService accountService, GameService gameService) {
        this.scoreRepository = scoreRepository;
        this.accountService = accountService;
        this.gameService = gameService;
    }

    @Override
    public void addScore(int userId, int points) {
        Account account = accountService.getAccountReferenceById(userId).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("Account with id=" + userId + " not found");
        }

        Game game = gameService.getGame();

        //todo: add getByGame too in future
        Score score = scoreRepository.findByAccountId(userId).orElse(new Score());
        score.setScore(score.getScore() + points);
        score.setAccount(account);
        score.setGame(game);
        scoreRepository.save(score);
    }
}
