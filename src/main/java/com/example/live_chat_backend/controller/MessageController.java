package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import com.example.live_chat_backend.service.MessageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/recent")
    public List<ChatMessageRequestDto> getRecentMessages() {
        return messageService.getRecentMessages(50);
    }
}
