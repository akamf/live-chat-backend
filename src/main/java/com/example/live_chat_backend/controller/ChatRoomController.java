package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.ChatRoomConnectionRepository;
import com.example.live_chat_backend.repository.ChatRoomRepository;
import com.example.live_chat_backend.repository.UserRepository;
import com.example.live_chat_backend.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final WebSocketSessionRegistry sessionRegistry;

    @GetMapping("{roomId}/online")
    public ResponseEntity<?> getOnlineUsers(@PathVariable Long roomId) {
        return ResponseEntity.ok(sessionRegistry.getUsersInRoom(String.valueOf(roomId)));
    }

    @GetMapping("/online-counts")
    public ResponseEntity<?> getOnlineCounts() {
        return ResponseEntity.ok(sessionRegistry.getOnlineCounts());
    }

    @PostMapping("/{roomId}/join/{userId}")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<ChatRoom> room = chatRoomRepository.findById(roomId);

        if (user.isEmpty() || room.isEmpty()) {
            log.warn("User or chat room not found. User={} : Room={}", user, room);
            return ResponseEntity.notFound().build();
        }

        if (!sessionRegistry.tryAddUser(String.valueOf(roomId), String.valueOf(userId))) {
            log.warn("Chat Room with ID {} is full", roomId);
            return ResponseEntity.status(403).body("Chat Room is full");
        }

        ChatRoomConnection connection = ChatRoomConnection.builder()
                .user(user.get())
                .chatRoom(room.get())
                .joinedAt(LocalDateTime.now())
                .build();

        connectionRepository.save(connection);
        log.info("User {} joined Room {}", userId, roomId);

        return ResponseEntity.ok("Joined Room");
    }

    @DeleteMapping("/{roomId}/leave/{userId}")
    public ResponseEntity<?> leaveRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        connectionRepository.deleteByUser_IdAndChatRoom_Id(userId, roomId);
        sessionRegistry.removeUser(String.valueOf(roomId), String.valueOf(userId));
        log.info("User {} left Room {}", userId, roomId);
        return ResponseEntity.ok("Left Room");
    }
}
