package com.example.live_chat_backend.service;

import com.example.live_chat_backend.dto.SystemMessage;
import com.example.live_chat_backend.entity.ChatRoom;
import com.example.live_chat_backend.entity.ChatRoomConnection;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.ChatRoomConnectionRepository;
import com.example.live_chat_backend.repository.ChatRoomRepository;
import com.example.live_chat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomEventService {

    private final ChatRoomRepository chatRoomRepo;
    private final UserRepository userRepo;
    private final ChatRoomConnectionRepository connectionRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public void connectUserToRoom(String userId, Long roomId) {
        ChatRoom room = chatRoomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (isRoomFull(room)) {
            throw new IllegalStateException("Chat room is full");
        }

        connectionRepo.save(ChatRoomConnection.builder()
                .chatRoom(room)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build()
        );
        sendSystemMessage(String.valueOf(roomId), "User " + user.getName() + " joined");

        log.debug("User {} connected to room {}", userId, roomId);
    }

    @Transactional
    public void disconnectUserFromRoom(String userId, Long roomId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        connectionRepo.deleteByUser_IdAndChatRoom_Id(userId, roomId);
        sendSystemMessage(String.valueOf(roomId), "User " + user.getName() + " left");
        log.debug("User {} disconnected from room {}", userId, roomId);
    }

    private void sendSystemMessage(String roomId, String content) {
        SystemMessage msg = new SystemMessage(content, LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/" + roomId, msg);
        log.info("[Room {}] {}", roomId, content);
    }

    private boolean isRoomFull(ChatRoom room) {
        long count = connectionRepo.countByChatRoom_Id(room.getId());
        return count >= room.getMaxUsers();
    }
}
