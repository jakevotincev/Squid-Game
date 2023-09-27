package ru.jakev.backend.services;

import ru.jakev.backend.entities.Account;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
public interface AccountService {
    //todo: возвращать дто здесь и везде в сервисах
    Optional<Account> getAccount(String username);
}
