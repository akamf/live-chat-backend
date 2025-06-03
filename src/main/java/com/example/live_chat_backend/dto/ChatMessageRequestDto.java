package com.example.live_chat_backend.dto;

public record ChatMessageRequestDto(
        String userId,
        String content,
        String roomId
) { }
