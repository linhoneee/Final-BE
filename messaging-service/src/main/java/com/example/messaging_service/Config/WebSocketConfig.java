//package com.example.messaging_service.Config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        logger.info("Registering STOMP endpoint at /ws");
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        logger.info("Configuring message broker with application destination prefix /app and simple broker /topic");
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic");
//    }
//
//    // Native WebSocket configuration
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        // Adding native WebSocket handler
//        logger.info("Registering native WebSocket endpoint at /native-ws");
//        registry.addHandler(textWebSocketHandler(), "/native-ws").setAllowedOrigins("*");
//    }
//
//    @Bean
//    public WebSocketHandler textWebSocketHandler() {
//        // Custom handler that could be extended further according to your needs
//        return new TextWebSocketHandler() {
//            @Override
//            protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
//                // Echo the message back to the client
//                session.sendMessage(message);
//            }
//        };
//    }
//}
