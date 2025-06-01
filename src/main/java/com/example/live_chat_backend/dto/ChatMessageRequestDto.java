package com.example.live_chat_backend.dto;

public record ChatMessageRequestDto(
        String sender,
        String content,
        String timestamp
) { }
