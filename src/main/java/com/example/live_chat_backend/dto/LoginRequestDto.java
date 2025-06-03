package com.example.live_chat_backend.dto;

import com.example.live_chat_backend.entity.User;

import java.time.LocalDateTime;

public record LoginRequestDto(
        String userId,
        String name,
        String email
) {
    public User toUser() {
        return User.builder()
                .id(this.userId())
                .email(this.email())
                .name(this.name())
                .isOnline(true)
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
