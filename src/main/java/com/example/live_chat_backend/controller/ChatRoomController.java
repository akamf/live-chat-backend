package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.ChatRoomRepository;
import com.example.live_chat_backend.repository.UserRepository;
import com.example.live_chat_backend.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/chat-rooms")
@CrossOrigin
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final WebSocketSessionRegistry sessionRegistry;

    @GetMapping("/public")
    public ResponseEntity<?> getAllPublicChatRooms() {
        List<ChatRoom> publicRooms = chatRoomRepository.findAll().stream()
                .filter(room -> !room.isPrivate())
                .toList();
        return ResponseEntity.ok(publicRooms);
    }

    @GetMapping("/{roomId}/online")
    public ResponseEntity<?> getOnlineUsers(@PathVariable Long roomId) {
        Set<String> activeUserIds = sessionRegistry.getUsersInRoom(roomId);
        List<User> users = activeUserIds.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/online-counts")
    public ResponseEntity<?> getOnlineCounts() {
        log.info("Current session registry: {}", sessionRegistry.getAll());
        Map<Long, Integer> counts = sessionRegistry.getAll().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));

        return ResponseEntity.ok(counts);
    }
}
