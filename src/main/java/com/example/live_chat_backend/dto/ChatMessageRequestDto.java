package com.example.live_chat_backend.dto;

import com.example.live_chat_backend.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageRequestDto(
        String sender,
        String content,
        String roomId
) {
    public ChatMessage toChatMessage() {
        return ChatMessage.builder()
                .sender(this.sender())
                .content(this.content())
                .timestamp(LocalDateTime.now())
                .roomId(this.roomId())
                .build();
    }
}
