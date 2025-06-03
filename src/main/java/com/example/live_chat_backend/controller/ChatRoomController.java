package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.repository.ChatRoomConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/chat-rooms")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomConnectionRepository connectionRepository;

    @GetMapping("/{roomId}/online")
    public ResponseEntity<?> getOnlineUsers(@PathVariable Long roomId) {
        var connections = connectionRepository.findByChatRoom_Id(roomId);
        var users = connections.stream().map(ChatRoomConnection::getUser).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/online-counts")
    public ResponseEntity<?> getOnlineCounts() {
        var counts = connectionRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        c -> c.getChatRoom().getId(),
                        Collectors.counting()
                ));
        return ResponseEntity.ok(counts);
    }
}
