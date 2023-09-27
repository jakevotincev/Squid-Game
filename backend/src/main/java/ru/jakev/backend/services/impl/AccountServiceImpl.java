package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.repositories.AccountRepository;
import ru.jakev.backend.services.AccountService;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccount(String username) {
        //todo: check if account didn't exist
        return Optional.ofNullable(accountRepository.findByName(username));
    }
}
