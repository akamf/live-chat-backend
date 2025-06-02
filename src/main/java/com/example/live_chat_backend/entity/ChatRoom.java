package com.example.live_chat_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String topic;
    private Integer maxUsers;
    private String accessUrl;
    private boolean isPrivate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
