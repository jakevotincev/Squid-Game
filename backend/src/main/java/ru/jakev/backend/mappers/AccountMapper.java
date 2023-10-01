package ru.jakev.backend.mappers;

import org.mapstruct.Mapper;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Account;

/**
 * @author evotintsev
 * @since 29.09.2023
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountToAccountDTO(Account account);
    Account accountDTOtoAccount(AccountDTO accountDTO);
}
