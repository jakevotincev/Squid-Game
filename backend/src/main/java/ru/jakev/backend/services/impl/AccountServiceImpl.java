package ru.jakev.backend.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Optional<AccountDTO> getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return Optional.empty();
        }

        return Optional.of(accountMapper.accountToAccountDTO(account));
    }

    @Override
    public void updateAccountParticipation(int accountId, boolean participated) {
        accountRepository.updateAccountParticipationById(accountId, participated);
    }

    @Override
    public boolean isAccountParticipatesInGame(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null){
            throw new UsernameNotFoundException(String.format("Account with id=%s not found", accountId));
        }

        return account.getParticipatesInGame();
    }

    @Override
    public boolean saveAccount(AccountDTO accountDTO) {
        try {
            getAccountByUsername(
                    accountDTO.getUsername()).ifPresent(account -> {
                throw new IllegalArgumentException(
                        String.format("Account with username=%s already exists", accountDTO.getUsername()));
            });

            accountRepository.save(accountMapper.accountDTOtoAccount(accountDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(String.format("Account with username=%s not found", username));
        }
        return account;
    }
}
