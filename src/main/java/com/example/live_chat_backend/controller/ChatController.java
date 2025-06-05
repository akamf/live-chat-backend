package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import com.example.live_chat_backend.dto.ChatMessageResponseDto;
import com.example.live_chat_backend.dto.SystemMessage;
import com.example.live_chat_backend.dto.TypingStatus;
import com.example.live_chat_backend.entity.ChatMessage;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.service.MessageService;
import com.example.live_chat_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Slf4j
@Controller
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/chat")
    public void handleIncomingMessage(@Payload ChatMessageRequestDto messageDto) {
        String roomId = messageDto.roomId();
        User sender = userService.findById(messageDto.userId());

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .content(messageDto.content())
                .timestamp(LocalDateTime.now())
                .roomId(roomId)
                .build();

        log.info("Received message from {} inside {}: {}", sender.getName(), roomId, message.getContent());

        messageService.saveMessage(message);
        messagingTemplate.convertAndSend(
                "/topic/" + roomId,
                ChatMessageResponseDto.fromEntity(message)
        );

        log.info("System Echo: {}", message.getContent());
    }

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingStatus event) {
        messagingTemplate.convertAndSend("/topic/" + event.roomId() + "/typing", event);
    }

    @MessageExceptionHandler
    public void handleException(Throwable exception) {
        log.error("WebSocket message error:", exception);
    }
}
