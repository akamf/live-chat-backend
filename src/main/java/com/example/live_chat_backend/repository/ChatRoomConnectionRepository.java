package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.ChatRoomConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomConnectionRepository extends JpaRepository<ChatRoomConnection, ChatRoomConnectionId> {
    List<ChatRoomConnection> findByChatRoom_Id(Long chatRoomId);
    List<ChatRoomConnection> findByUser_Id(Long userId);
    void deleteByUser_IdAndChatRoom_Id(Long userId, Long ChatRoomId);
}
