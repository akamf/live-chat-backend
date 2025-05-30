package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessage;
import com.example.live_chat_backend.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            MessageService messageService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    public void handleIncomingMessage(ChatMessage message) {
        log.info("Received message from {}: {}", message.sender(), message.content());

        messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/chat", message);

        ChatMessage fallback = new ChatMessage("System", "Echo: " + message.content(), message.timestamp());
        messagingTemplate.convertAndSend("/topic/chat", fallback);
    }
}
