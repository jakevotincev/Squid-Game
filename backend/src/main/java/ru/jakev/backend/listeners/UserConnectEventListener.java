package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import ru.jakev.backend.game.GlobalContext;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.services.AccountService;

import java.security.Principal;

/**
 * @author evotintsev
 * @since 21.02.2024
 */
@Component
public class UserConnectEventListener implements ApplicationListener<SessionConnectedEvent> {
    private final GlobalContext globalContext;
    private final AccountService accountService;
    private final Logger LOG = LoggerFactory.getLogger(UserConnectEventListener.class);

    public UserConnectEventListener(GlobalContext globalContext, AccountService accountService) {
        this.globalContext = globalContext;
        this.accountService = accountService;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            AccountDTO account = accountService.getAccountByUsername(user.getName()).orElse(null);
            //todo: что будет если зайдет аноним?
            if (account == null) {
                LOG.error("User {} not found", user.getName());
            }
            if (account != null) {
                LOG.info("{} Connected", account.getUsername());
            }

            globalContext.addConnectedUser(user, account);
        } else {
            LOG.info("Anonymous user connected");
        }
    }
}
