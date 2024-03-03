package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.entities.Score;
import ru.jakev.backend.mappers.AccountMapper;
import ru.jakev.backend.repositories.ScoreRepository;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.GameService;
import ru.jakev.backend.services.ScoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author evotintsev
 * @since 01.03.2024
 */
@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    private final GameService gameService;

    public ScoreServiceImpl(ScoreRepository scoreRepository, AccountService accountService, AccountMapper accountMapper, GameService gameService) {
        this.scoreRepository = scoreRepository;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
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

    @Override
    public Optional<Score> getScoreByUserId(int userId) {
        return scoreRepository.findByAccountId(userId);
    }

    @Override
    public Map<AccountDTO, Integer> getResultsForUser(int userId) {
        Account account = accountService.getAccountById(userId).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("Account with id=" + userId + " not found");
        }

        Map<AccountDTO, Integer> results = new HashMap<>();
        Role role = account.getRole();

        if (role == Role.GLAVNIY || role == Role.MANAGER) {
            //todo: супер хреновая производительность, переделать
            List<Account> accounts = accountService.getAllAccounts();
            accounts.forEach(userAccount -> {
                if (userAccount.getRole() != Role.GLAVNIY && userAccount.getRole() != Role.MANAGER) {
                    Score score = scoreRepository.findByAccountId(userAccount.getId()).orElse(null);
                    results.put(accountMapper.accountToAccountDTO(userAccount), score == null ? 0 : score.getScore());
                }
            });
        } else {
            List<Account> accounts = accountService.getAccountsByRole(role);
            accounts.forEach(acc -> {
                Score score = scoreRepository.findByAccountId(acc.getId()).orElse(null);
                results.put(accountMapper.accountToAccountDTO(acc), score == null ? 0 : score.getScore());
            });
        }

        return results;
    }
}
