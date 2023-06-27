package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jakev.backend.entities.Account;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
