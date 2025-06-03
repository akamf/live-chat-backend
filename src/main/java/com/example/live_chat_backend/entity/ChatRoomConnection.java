package com.example.live_chat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_connections")
@IdClass(ChatRoomConnectionId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoomConnection {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private LocalDateTime joinedAt;
}
