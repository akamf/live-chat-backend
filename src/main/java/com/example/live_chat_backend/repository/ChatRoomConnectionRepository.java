package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.ChatRoomConnectionId;
import com.example.live_chat_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomConnectionRepository extends JpaRepository<ChatRoomConnection, ChatRoomConnectionId> {
    List<ChatRoomConnection> findByChatRoom_Id(Long chatRoomId);
    List<ChatRoomConnection> findByUser_Id(String userId);
    void deleteByUser_IdAndChatRoom_Id(String userId, Long ChatRoomId);

    int countByChatRoom_Id(Long roomId);

    Optional<ChatRoomConnection> findByUserAndChatRoom(User user, ChatRoom chatRoom);}
