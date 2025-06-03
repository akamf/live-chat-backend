package com.example.live_chat_backend.websocket;

import com.example.live_chat_backend.dto.SystemMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final WebSocketSessionRegistry registry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getFirstNativeHeader("user-id");
        String roomId = accessor.getFirstNativeHeader("room-id");

        if (roomId == null || userId == null) {
            log.error("Missing room-id or user-id. room-id={}, user-id={}", roomId, userId);
            return;
        }

        if (!registry.tryAddUser(Long.valueOf(roomId), userId)) {
            log.warn("User limit reached in room: {}", roomId);
            throw new IllegalStateException("Room full");
        }

        log.info("User connected: {} to room {}", userId, roomId);
        simpMessagingTemplate.convertAndSend(
                "/topic/" + roomId,
                new SystemMessage(
                        "User " + userId + " joined",
                        LocalDateTime.now().toString()
                )
        );
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getFirstNativeHeader("user-id");
        String roomId = accessor.getFirstNativeHeader("room-id");

        log.info("User disconnected: {} from room {}", userId, roomId);
        simpMessagingTemplate.convertAndSend(
                "/topic/" + roomId,
                new SystemMessage(
                        "User " + userId + " left",
                        LocalDateTime.now().toString()
                )
        );
    }
}
