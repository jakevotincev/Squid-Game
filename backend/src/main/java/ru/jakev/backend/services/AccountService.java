package ru.jakev.backend.services;

import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
public interface AccountService {
    //todo: возвращать дто здесь и везде в сервисах
    Optional<AccountDTO> getAccount(String username);

    void updateAccountParticipation(int accountId, boolean participated);
}
