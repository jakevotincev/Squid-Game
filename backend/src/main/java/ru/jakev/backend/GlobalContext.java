package ru.jakev.backend;

import com.sun.security.auth.UserPrincipal;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.services.CriteriaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class GlobalContext {
    private Integer playersCount;
    private final CriteriaService criteriaService;

    private final Map<Account, UserPrincipal> connectedUsers = new HashMap<>();

    public GlobalContext(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    public Integer getPlayersNumber() {
        if (playersCount == null) {
            Criteria criteria = criteriaService.getCriteria(1).orElse(null);
            playersCount = criteria != null ? criteria.getPlayersNumber() : 0;
        }
        return playersCount;
    }

    public void addConnectedUser(Account account, UserPrincipal userPrincipal) {
        connectedUsers.put(account, userPrincipal);
    }

    public UserPrincipal getUser(Account account) {
        return connectedUsers.get(account);
    }

    public Account getAccount(UserPrincipal userPrincipal) {
        return connectedUsers.entrySet().stream().filter((entry)-> entry.getValue()
                .equals(userPrincipal)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public Set<Account> getConnectedAccounts() {
        return connectedUsers.keySet();
    }

    public Set<UserPrincipal> getConnectedUsersByCriteria(Predicate<Account> predicate) {
        return connectedUsers.entrySet().stream()
                .filter((entry) -> predicate.test(entry.getKey())).map(Map.Entry::getValue)
                .collect(Collectors.toSet());

    }
}
