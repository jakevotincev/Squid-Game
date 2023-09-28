package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.mappers.AccountMapper;
import ru.jakev.backend.repositories.AccountRepository;
import ru.jakev.backend.services.AccountService;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDTO> getAccount(String username) {
        Account account = accountRepository.findByName(username);
        if (account == null) {
            return Optional.empty();
        }
        accountMapper.accountToAccountDTO(account);
        return Optional.of(accountMapper.accountToAccountDTO(account));
    }

    @Override
    public void updateAccountParticipation(int accountId, boolean participated) {
        accountRepository.updateAccountParticipationById(accountId, participated);
    }
}
