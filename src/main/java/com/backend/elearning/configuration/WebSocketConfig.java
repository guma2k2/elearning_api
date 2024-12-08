package com.backend.elearning.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Topic for broadcasting
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client requests
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Plain WebSocket endpoint
                .setAllowedOrigins("*"); // Allow frontend origin

        registry.addEndpoint("/sockjs/ws") // SockJS fallback endpoint
                .setAllowedOrigins("*")
                .withSockJS();
    }
}