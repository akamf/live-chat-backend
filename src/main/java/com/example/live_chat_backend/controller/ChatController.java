package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import com.example.live_chat_backend.dto.ChatMessageResponseDto;
import com.example.live_chat_backend.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

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
    public void handleIncomingMessage(
            @DestinationVariable String roomId,
            @Payload ChatMessageRequestDto message
    ) {
        log.info("Received message from {} inside {}: {}", message.sender(), roomId, message.content());

        messageService.saveMessage(new ChatMessageRequestDto(message.sender(), message.content(), roomId));
        messagingTemplate.convertAndSend("/topic/chat", message);

        ChatMessageResponseDto callback = new ChatMessageResponseDto(
                "System",
                "Echo: " + message.content(),
                LocalDateTime.now().toString(),
                roomId
        );

        messagingTemplate.convertAndSend("/topic/chat", callback);
    }
}
