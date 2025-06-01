package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop20ByRoomIdOrderByTimestampDesc(String roomId);
}
