package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessageResponseDto;
import com.example.live_chat_backend.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getRecentMessages(@PathVariable String roomId) {
        List<ChatMessageResponseDto> result = messageService.getRecentMessages(roomId);
        return ResponseEntity.ok(result);
    }

}
