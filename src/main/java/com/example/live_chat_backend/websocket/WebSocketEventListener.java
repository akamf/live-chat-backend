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
        String roomId = accessor.getFirstNativeHeader("room-id");

        if (!registry.tryAddUser(roomId, userId)) {
            log.warn("User limit reached in room: " + roomId);
            throw new IllegalStateException("Room full");
        }

        log.info("User connected: {} to room {}", userId, roomId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUser(accessor);
        String roomId = accessor.getFirstNativeHeader("room-id");

        registry.removeUser(userId, roomId);
        log.info("User disconnected: {} from room {}", userId, roomId);
    }

    private String getUser(StompHeaderAccessor accessor) {
        return accessor.getUser() != null ? accessor.getUser().getName() : accessor.getSessionId();
    }
}
