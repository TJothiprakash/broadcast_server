package com.broadcast_server.in.broadcast_server.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.*;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final ObjectMapper objectMapper = new ObjectMapper(); // JSON Serializer

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New client connected: " + session.getId());

        // Send JSON message for user joining
        broadcastMessage(Map.of("type", "join", "user", "User-" + session.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());

        // Send JSON formatted message
        broadcastMessage(Map.of("type", "message", "user", "User-" + session.getId(), "text", message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());

        // Send JSON message for user leaving
        broadcastMessage(Map.of("type", "leave", "user", "User-" + session.getId()));
    }

    private void broadcastMessage(Map<String, String> messageData) throws Exception {
        String jsonMessage = objectMapper.writeValueAsString(messageData);
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }
}
