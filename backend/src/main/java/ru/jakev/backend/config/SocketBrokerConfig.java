package ru.jakev.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.jakev.backend.messages.DefaultWebSocketMessageSender;
import ru.jakev.backend.messages.WebSocketMessageSender;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements
        WebSocketMessageBrokerConfigurer {

    private final UserHandshakeHandler userHandshakeHandler;

    public SocketBrokerConfig(UserHandshakeHandler userHandshakeHandler) {
        this.userHandshakeHandler = userHandshakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/glavniy", "/manager", "/worker", "/player", "/soldier");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/squid-game-socket").setHandshakeHandler(userHandshakeHandler).setAllowedOriginPatterns("*");
    }

    @Bean
    public WebSocketMessageSender simpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate){
        simpMessagingTemplate.setMessageConverter(new GsonMessageConverter());
        return new DefaultWebSocketMessageSender(simpMessagingTemplate);
    }
}
