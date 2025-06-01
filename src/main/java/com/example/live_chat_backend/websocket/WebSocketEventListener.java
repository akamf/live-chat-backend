package com.example.live_chat_backend.websocket;

import com.example.live_chat_backend.exception.WebSocketLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final WebSocketSessionRegistry registry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUser(accessor);

        if (!registry.tryAddUser(userId)) {
            log.warn("User limit reached. Reject connection for: {}", userId);
            simpMessagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/errors",
                    "User limit of 8 reached. Connection denied."
            );

            throw new WebSocketLimitException("User limit exceeded for userId=" + userId);
        }

        log.info("User connected: {}", userId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUser(accessor);

        registry.removeUser(userId);
        log.info("User disconnected: {}", userId);
    }

    private String getUser(StompHeaderAccessor accessor) {
        return accessor.getUser() != null ? accessor.getUser().getName() : accessor.getSessionId();
    }
}
