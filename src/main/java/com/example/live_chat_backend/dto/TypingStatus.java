package com.example.live_chat_backend.dto;

public record TypingStatus(
        String userId,
        String username,
        Long roomId,
        boolean typing
) {}