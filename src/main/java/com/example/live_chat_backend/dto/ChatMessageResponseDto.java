package com.example.live_chat_backend.dto;

import com.example.live_chat_backend.entity.ChatMessage;

public record ChatMessageResponseDto(
        String sender,
        String senderId,
        String content,
        String timestamp,
        String roomId
) {
    public static ChatMessageResponseDto fromEntity(ChatMessage entity) {
        return new ChatMessageResponseDto(
                entity.getSender().getName(),
                entity.getSender().getId(),
                entity.getContent(),
                entity.getTimestamp().toString(),
                entity.getRoomId()
        );
    }
}
