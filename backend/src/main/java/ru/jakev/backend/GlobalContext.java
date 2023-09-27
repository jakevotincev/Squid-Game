package ru.jakev.backend;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Account;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.listeners.FormListener;
import ru.jakev.backend.services.CriteriaService;

import java.util.HashMap;
import java.util.HashSet;
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
    private final CriteriaService criteriaService;
    private final Map<Account, UserPrincipal> connectedUsers = new HashMap<>();
    private final Set<Integer> acceptedForms = new HashSet<>();

    private final Logger LOG = LoggerFactory.getLogger(FormListener.class);

    public GlobalContext(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    public int getConnectedPlayersCount() {
        return getConnectedUsersByCriteria(account -> account.getRole() == Role.PLAYER).size();
    }

    public int getAcceptedPlayersCount(){
        Criteria criteria = criteriaService.getCriteria(1).orElse(null);
        return criteria != null ? criteria.getPlayersNumber() : 0;
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

    /**
     * @param playerId player id
     * @return true if all forms accepted
     */
    public boolean acceptForm(int playerId) {
        acceptedForms.add(playerId);
        //todo: подумать как защититься от ситуации, когда отправили больше форм чем надо
        return getAcceptedPlayersCount() == acceptedForms.size();
    }
}
