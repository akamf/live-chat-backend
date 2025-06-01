package com.example.live_chat_backend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final WebSocketSessionRegistry registry;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getUser() != null ? accessor.getUser().getName() : accessor.getSessionId();

        if (!registry.tryAddUser(userId)) {
            log.warn("User limit reached. Reject connection for: {}", userId);
        } else {
            log.info("User connected: {}", userId);
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getUser() != null ? accessor.getUser().getName() : accessor.getSessionId();

        registry.removeUser(userId);
        log.info("User disconnected: {}", userId);
    }

}
