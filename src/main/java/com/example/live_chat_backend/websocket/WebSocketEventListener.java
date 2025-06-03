package com.example.live_chat_backend.websocket;

import com.example.live_chat_backend.dto.SystemMessage;
import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.ChatRoomConnectionRepository;
import com.example.live_chat_backend.repository.ChatRoomRepository;
import com.example.live_chat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @EventListener
    @Transactional(readOnly = true)
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = accessor.getFirstNativeHeader("user-id");
        String roomId = accessor.getFirstNativeHeader("room-id");

        if (roomId == null || userId == null) {
            log.error("Missing room-id or user-id. room-id={}, user-id={}", roomId, userId);
            return;
        }

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("room-id", roomId);
            sessionAttributes.put("user-id", userId);
        }

        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(Long.valueOf(roomId));
        Optional<User> userOpt = userRepository.findById(userId);

        if (chatRoomOpt.isEmpty() || userOpt.isEmpty()) {
            log.warn("ChatRoom or User not found. roomId={}, userId={}", roomId, userId);
            return;
        }

        ChatRoom chatRoom = chatRoomOpt.get();
        User user = userOpt.get();

        long currentUsers = connectionRepository.countByChatRoom_Id(chatRoom.getId());
        if (currentUsers >= chatRoom.getMaxUsers()) {
            log.warn("Chat room {} is full ({} users)", chatRoom.getId(), currentUsers);
            throw new IllegalStateException("Room full");
        }

        ChatRoomConnection connection = ChatRoomConnection.builder()
                .chatRoom(chatRoom)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        connectionRepository.save(connection);

        simpMessagingTemplate.convertAndSend(
                "/topic/" + roomId,
                new SystemMessage(String.format("User %s joined",user.getName()), LocalDateTime.now().toString())
        );
        log.debug("Connect: sessionId={}, room-id={}, user-id={}",
                accessor.getSessionId(), roomId, userId);
    }

    @EventListener
    @Transactional
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        String roomId = sessionAttributes != null ? (String) sessionAttributes.get("room-id") : null;
        String userId = sessionAttributes != null ? (String) sessionAttributes.get("user-id") : null;

        if (roomId == null || userId == null) return;

        connectionRepository.deleteByUser_IdAndChatRoom_Id(userId, Long.valueOf(roomId));

        log.debug("Disconnect: sessionId={}, room-id={}, user-id={}",
                accessor.getSessionId(), roomId, userId);

        simpMessagingTemplate.convertAndSend(
                "/topic/" + roomId,
                new SystemMessage(String.format("User with ID %s left", userId), LocalDateTime.now().toString())
        );
    }
}
