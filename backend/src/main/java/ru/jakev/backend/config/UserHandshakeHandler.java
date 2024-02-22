package ru.jakev.backend.config;

import com.sun.security.auth.UserPrincipal;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.services.AccountService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);
    private final AccountService accountService;

    public UserHandshakeHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, @Nonnull WebSocketHandler wsHandler,
                                      @Nonnull Map<String, Object> attributes) {
        Map<String, String> paramsMap = getQueryMap(request.getURI().getQuery());
        String username = paramsMap.get("username");
        if (username == null) {
            LOG.error("Username header doesn't exists");
            return null;
        }
        AccountDTO account = accountService.getAccountByUsername(username).orElse(null);
        //todo: что будет если зайдет аноним?
        if (account == null) {
            LOG.error("User {} not found", username);
            return null;
        }

        return new UserPrincipal(username);
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
