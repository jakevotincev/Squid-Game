package ru.jakev.backend.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Role;

import java.util.List;
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
    Optional<Account> getAccountReferenceById(int id);

    Optional<Account> getAccountById(int id);
    List<Account> getAccountsByRole(Role role);
    List<Account> getAllAccounts();
}
