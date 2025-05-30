package com.example.live_chat_backend.dto;

public record ChatMessage(
        String sender,
        String content,
        String timestamp
) { }
