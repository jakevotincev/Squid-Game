package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.game.GamePhase;
import ru.jakev.backend.game.PhaseManager;
import ru.jakev.backend.listeners.LunchListener;
import ru.jakev.backend.services.AccountService;

import java.util.EnumSet;

/**
 * @author evotintsev
 * @since 29.09.2023
 */
@RestController
@CrossOrigin
public class AccountController {
    private final LunchListener lunchListener;
    private final AccountService accountService;
    private final PhaseManager phaseManager;

    public AccountController(LunchListener lunchListener, AccountService accountService, PhaseManager phaseManager) {
        this.lunchListener = lunchListener;
        this.accountService = accountService;
        this.phaseManager = phaseManager;
    }

    //todo: сделать защиту от получения данных других аккаунтов
    @GetMapping("/account/{username}")
    public ResponseEntity<?> getAccount(@PathVariable("username") String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username not set");
        }
        AccountDTO account = accountService.getAccountByUsername(username).orElse(null);
        return account == null ? ResponseEntity.badRequest().body(String.format("Account with username = %s not found", username)) :
                ResponseEntity.ok(account);
    }

    @GetMapping("/account/{id}/readyToGame")
    public ResponseEntity<String> readyToGame(@PathVariable("id") int id) {
        Account account = accountService.getAccountById(id).orElseGet(null);
        //todo: проверки перенести в другое место
        if (account == null) {
            return ResponseEntity.badRequest().body(String.format("Account with id = %d not found", id));
        }

        if (phaseManager.isActionNotPermitted(EnumSet.of(GamePhase.LUNCH_EATING, GamePhase.LUNCH_MAKING))) {
            return ResponseEntity.badRequest().body(String.format("Ready to game action is not permitted now. Current game phase is %s",
                    phaseManager.getCurrentPhase()));
        }

        if (phaseManager.getPermittedRoleByPhase() != account.getRole()) {
            return ResponseEntity.badRequest().body(String.format("This role:%s cannot make this action in phase:%s ",
                    account.getRole(), phaseManager.getCurrentPhase()));
        }

        boolean ready = lunchListener.userReadyToGame(account, phaseManager.getCurrentPhase(), phaseManager::startNextPhase);
        return ready ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Player not actually ready");
    }
}
