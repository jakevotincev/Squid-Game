package ru.jakev.backend.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.dto.AccountDTO;

import java.security.Principal;

/**
 * @author evotintsev
 * @since 01.10.2023
 */
@Component
public class UserDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final GlobalContext globalContext;
    private final Logger LOG = LoggerFactory.getLogger(UserDisconnectEventListener.class);

    public UserDisconnectEventListener(GlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            AccountDTO account = globalContext.getAccount(user);
            if (account != null) {
                LOG.info("{} disconnected", account.getUsername());
            }

            globalContext.removeConnectedUser(user);
        } else {
            LOG.info("Anonymous user disconnected");
        }
    }
}
