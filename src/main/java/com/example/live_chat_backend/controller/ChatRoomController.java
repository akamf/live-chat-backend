package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.websocket.WebSocketSessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/chat-rooms")
public class ChatRoomController {

    private final WebSocketSessionRegistry sessionRegistry;

    public ChatRoomController(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/online")
    public Map<String, Integer> getOnlineUsersPerRoom() {
        return sessionRegistry.getOnlineCounts();
    }
}
