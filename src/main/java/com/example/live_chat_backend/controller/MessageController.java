package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessageResponseDto;
import com.example.live_chat_backend.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatMessageResponseDto>> getRecentMessages(@PathVariable String roomId) {
        List<ChatMessageResponseDto> result = messageService.getRecentMessages(roomId);
        return ResponseEntity.ok(result);
    }

}
