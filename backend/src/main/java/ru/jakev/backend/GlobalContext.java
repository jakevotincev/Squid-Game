package ru.jakev.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.services.CriteriaService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class GlobalContext {
    private final CriteriaService criteriaService;
    //todo: thing about make thread safe
    private final Map<Principal, AccountDTO> connectedUsers = new ConcurrentHashMap<>();
    private final Set<Integer> acceptedForms = new HashSet<>();
    private final Logger LOG = LoggerFactory.getLogger(GlobalContext.class);


    public GlobalContext(CriteriaService criteriaService,
                         @Value("${show_user_stats.enabled}") Boolean showUserStatsEnabled,
                         @Value("${show_user_stats.period}") int period) {
        this.criteriaService = criteriaService;
        if (showUserStatsEnabled) {
            ScheduledExecutorService userStatsExecutorService = Executors.newScheduledThreadPool(1);
            userStatsExecutorService.scheduleAtFixedRate(this::showLog, 0, period, TimeUnit.SECONDS);
        }
    }

    private void showLog() {
        LOG.info("\n Connected users info: \n count: {} \n{}", connectedUsers.size(),
                getConnectedUsersString());
    }

    public int getConnectedPlayersCount() {
        return getConnectedUsersByCriteria(account -> account.getRole() == Role.PLAYER).size();
    }

    public int getAcceptedPlayersCount() {
        Criteria criteria = criteriaService.getCriteria(1).orElse(null);
        return criteria != null ? criteria.getPlayersNumber() : 0;
    }

    public void addConnectedUser(Principal userPrincipal, AccountDTO account) {
        connectedUsers.put(userPrincipal, account);
        showLog();
    }

    public void removeConnectedUser(Principal userPrincipal) {
        connectedUsers.remove(userPrincipal);
        showLog();
    }

    public Principal getPrincipalById(long accountId) {
        Principal principal = connectedUsers.entrySet().stream().filter(entry -> entry.getValue().getId() == accountId)
                .map(Map.Entry::getKey).findFirst().orElse(null);

        if (principal == null) {
            throw new IllegalArgumentException(String.format("Account with id:%s not connected", accountId));
        }
        return principal;
    }


    public AccountDTO getAccount(Principal userPrincipal) {
        return connectedUsers.get(userPrincipal);
    }

    public Set<Principal> getConnectedUsersByCriteria(Predicate<AccountDTO> predicate) {
        return connectedUsers.entrySet().stream()
                .filter((entry) -> predicate.test(entry.getValue())).map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    private String getConnectedUsersString() {
        StringBuilder connectedUsersStr = new StringBuilder();
        connectedUsers.forEach((key, value) -> {
            connectedUsersStr.append(String.format(" User id:%s name:%s role:%s\n", value.getId(), value.getName(), value.getRole()));
        });

        return connectedUsersStr.isEmpty() ? "" : connectedUsersStr.substring(0, connectedUsersStr.length() - 1);
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
