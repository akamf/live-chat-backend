package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.ChatRoomConnectionRepository;
import com.example.live_chat_backend.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/chat-rooms")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomConnectionRepository connectionRepository;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/{roomId}/online")
    public ResponseEntity<?> getOnlineUsers(@PathVariable Long roomId) {
        List<ChatRoomConnection> connections = connectionRepository.findByChatRoom_Id(roomId);
        List<User> users = connections.stream().map(ChatRoomConnection::getUser).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/online-counts")
    public ResponseEntity<?> getOnlineCounts() {
        Map<Long, Long> counts = connectionRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        c -> c.getChatRoom().getId(),
                        Collectors.counting()
                ));
        return ResponseEntity.ok(counts);
    }
    @GetMapping("/public")
    public ResponseEntity<?> getAllPublicChatRooms() {
        List<ChatRoom> publicRooms = chatRoomRepository.findAll().stream()
                .filter(room -> !room.isPrivate())
                .toList();
        return ResponseEntity.ok(publicRooms);
    }

}
