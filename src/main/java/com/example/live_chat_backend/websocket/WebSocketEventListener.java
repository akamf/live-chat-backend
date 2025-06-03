package com.example.live_chat_backend.websocket;

import com.example.live_chat_backend.service.ChatRoomEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatRoomEventService eventService;

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getFirstNativeHeader("user-id");
        String roomId = accessor.getFirstNativeHeader("room-id");

        if (userId == null || roomId == null) {
            log.error("Missing headers user-id or room-id");
            return;
        }

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("user-id", userId);
            sessionAttributes.put("room-id", roomId);
        }

        try {
            eventService.connectUserToRoom(userId, Long.valueOf(roomId));
            eventService.sendSystemMessage(roomId, "User " + userId + " joined");
        } catch (Exception e) {
            log.warn("Connection failed: {}", e.getMessage());
        }
    }

    @EventListener
    @Transactional
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        String userId = sessionAttributes != null ? (String) sessionAttributes.get("user-id") : null;
        String roomId = sessionAttributes != null ? (String) sessionAttributes.get("room-id") : null;

        if (userId == null || roomId == null) return;

        eventService.disconnectUserFromRoom(userId, Long.valueOf(roomId));
        eventService.sendSystemMessage(roomId, "User " + userId + " left");
    }
}
