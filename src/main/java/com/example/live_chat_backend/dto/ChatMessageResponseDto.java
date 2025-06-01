package com.example.live_chat_backend.dto;

public record ChatMessageResponseDto(
        String sender,
        String content,
        String timestamp,
        String roomId
) { }
