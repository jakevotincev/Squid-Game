package ru.jakev.backend.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
public interface AccountService extends UserDetailsService {
    //todo: возвращать дто здесь и везде в сервисах
    Optional<AccountDTO> getAccountByUsername(String username);

    void updateAccountParticipation(int accountId, boolean participated);

    boolean isAccountParticipatesInGame(Integer accountId);

    boolean saveAccount(Account accountDTO);

    void updateAccountRole(AccountDTO account);
}
