package ru.jakev.backend.config;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.jakev.backend.GlobalContext;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.JwtService;

/**
 * @author evotintsev
 * @since 20.02.2024
 */
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final GlobalContext globalContext;

    private final Logger LOG = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

    public WebSocketAuthInterceptor(JwtService jwtService, AccountService accountService, GlobalContext globalContext) {
        this.jwtService = jwtService;
        this.accountService = accountService;
        this.globalContext = globalContext;
    }

    //todo: подумать над ексепшон хэндлингом
    //todo: подумать над логированием, бага если чел отключился с ошибкой то он не удаляется из соединенных юзеров
    //todo: добавить в сообщения тип
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var cmd = accessor.getCommand();
        String jwt = null;
        if (StompCommand.CONNECT == cmd || StompCommand.SEND == cmd || StompCommand.SUBSCRIBE == cmd||
                StompCommand.DISCONNECT == cmd) {
            try {
                final var requestTokenHeader = accessor.getFirstNativeHeader("Authorization");
                if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
                    jwt = requestTokenHeader.substring(7);
                }

                String username = jwtService.extractUserName(jwt);

                UserDetails userDetails = accountService
                        .loadUserByUsername(username);
                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    LOG.error("Token is not valid");
                    throw new SecurityException("Token is not valid");
                }
            } catch (Exception e) {
                globalContext.removeConnectedUser(accessor.getUser());
                LOG.error("Error while authenticate message", e);
                throw new SecurityException("Error while processing message");
            }

        }
        return message;
    }
}
