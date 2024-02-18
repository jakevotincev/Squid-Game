package ru.jakev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.jakev.backend.entities.Account;

import java.util.Optional;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    @Modifying
    @Query("update Account a set a.participatesInGame=:participatesInGame where a.id=:id")
    void updateAccountParticipationById(@Param("id") Integer id, @Param("participatesInGame") boolean participatesInGame);

    Optional<Account> findById(Integer id);
}
