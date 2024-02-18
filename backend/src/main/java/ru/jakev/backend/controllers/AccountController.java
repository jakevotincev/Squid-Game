package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.services.AccountService;

/**
 * @author evotintsev
 * @since 29.09.2023
 */
@RestController
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/{username}")
    public ResponseEntity<?> getAccount(@PathVariable("username") String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username not set");
        }
        AccountDTO account = accountService.getAccountByUsername(username).orElse(null);
        return account == null ? ResponseEntity.badRequest().body(String.format("Account with username = %s not found", username)) :
                ResponseEntity.ok(account);
    }
}
