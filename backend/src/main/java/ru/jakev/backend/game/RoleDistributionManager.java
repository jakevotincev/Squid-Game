package ru.jakev.backend.game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.services.AccountService;

import java.util.*;

/**
 * @author evotintsev
 * @since 22.02.2024
 */
@Component
public class RoleDistributionManager {
    @Value("${game.min-users-count}")
    private static final int MIN_USERS_COUNT = 8;
    private final GlobalContext globalContext;
    private final AccountService accountService;
    //todo : добавить проперти для этого
    private static final double PLAYERS_PROPORTION = 0.5;
    private static final double SOLDIERS_PROPORTION = 0.25;

    public RoleDistributionManager(GlobalContext globalContext, AccountService accountService) {
        this.globalContext = globalContext;
        this.accountService = accountService;
    }

    //todo: стоит ли тут возвращать лист с распределенными ролями?
    public boolean distributeRoles() {
        List <AccountDTO> connectedUndefinedUsers = globalContext.getConnectedUsers().values().stream()
                .filter(accountDTO -> accountDTO.getRole() == Role.UNDEFINED).toList();
        int usersCount = connectedUndefinedUsers.size();

        if (!validateUsersCount(usersCount)) {
            return false;
        }

        ArrayList<AccountDTO> shuffledAccounts = new ArrayList<>(connectedUndefinedUsers);
        Collections.shuffle(shuffledAccounts);
        assignRoles((int) Math.floor((usersCount * PLAYERS_PROPORTION)), Role.PLAYER, shuffledAccounts);
        assignRoles((int) Math.ceil((usersCount * SOLDIERS_PROPORTION)), Role.SOLDIER, shuffledAccounts);
        assignRoles(shuffledAccounts.size(), Role.WORKER, shuffledAccounts);

        return true;
    }

    /**
     * Assign specified role to specified amount of users and deletes them from list
     *
     * @param usersCount users count
     * @param role role
     * @param users list of users
     */
    private void assignRoles(int usersCount, Role role, List<AccountDTO> users) {
        for (int i = 0; i < usersCount; i++) {
            Iterator<AccountDTO> iterator = users.iterator();
            AccountDTO account = iterator.next();
            account.setRole(role);
            if (role != Role.PLAYER) {
                accountService.updateAccountParticipation(account.getId(), true);
            }
            accountService.updateAccountRole(account);
            iterator.remove();
        }
    }

    private boolean validateUsersCount(int usersCount) {
        return usersCount >= MIN_USERS_COUNT;
    }
}
