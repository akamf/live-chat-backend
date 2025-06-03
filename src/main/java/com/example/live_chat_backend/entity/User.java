package com.example.live_chat_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;
    private String password;
    private LocalDateTime lastLogin;
    private boolean isOnline;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
