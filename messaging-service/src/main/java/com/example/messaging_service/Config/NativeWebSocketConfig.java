package com.example.messaging_service.Config;

import com.example.messaging_service.Model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocket
public class NativeWebSocketConfig implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(NativeWebSocketConfig.class);

    private final Map<String, Map<String, org.springframework.web.socket.WebSocketSession>> sessionsByRoom = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info("Registering native WebSocket endpoint at /native-ws");
        registry.addHandler(textWebSocketHandler(), "/native-ws").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler textWebSocketHandler() {
        return new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session) throws Exception {
                String roomId = session.getHandshakeHeaders().getFirst("RoomID");
                if (roomId != null) {
                    registerSession(roomId, session);
                }
            }

            @Override
            protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
                // Xử lý tin nhắn, nhưng không gửi lại trong handler này
                ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
                String roomId = chatMessage.getRoomId().toString();
                registerSession(roomId, session);

                // Tin nhắn sẽ được gửi lại từ Controller, không phải từ đây
            }

            @Override
            public void afterConnectionClosed(org.springframework.web.socket.WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
                String roomId = (String) session.getAttributes().get("roomId");
                if (roomId != null) {
                    unregisterSession(roomId, session);
                }
            }
        };
    }

    private void registerSession(String roomId, org.springframework.web.socket.WebSocketSession session) {
        sessionsByRoom.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>()).put(session.getId(), session);
        session.getAttributes().put("roomId", roomId);
    }

    private void unregisterSession(String roomId, org.springframework.web.socket.WebSocketSession session) {
        Map<String, org.springframework.web.socket.WebSocketSession> sessions = sessionsByRoom.get(roomId);
        if (sessions != null) {
            sessions.remove(session.getId());
            if (sessions.isEmpty()) {
                sessionsByRoom.remove(roomId);
            }
        }
    }

    // Phương thức này sẽ được sử dụng trong các phương thức Controller
    public void sendMessageToRoom(String roomId, String message) throws Exception {
        Map<String, org.springframework.web.socket.WebSocketSession> sessions = sessionsByRoom.get(roomId);
        if (sessions != null) {
            for (org.springframework.web.socket.WebSocketSession session : sessions.values()) {
                session.sendMessage(new org.springframework.web.socket.TextMessage(message));
            }
        }
    }
}