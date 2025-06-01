package com.example.live_chat_backend.service;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import com.example.live_chat_backend.dto.ChatMessageResponseDto;
import com.example.live_chat_backend.entity.ChatMessage;
import com.example.live_chat_backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public void saveMessage(ChatMessageRequestDto dto) {
        ChatMessage entity = dto.toChatMessage();
        repository.save(entity);
    }

    public List<ChatMessageResponseDto> getRecentMessages(String roomId) {
        return repository.findTop20ByRoomIdOrderByTimestampDesc(roomId)
                .stream()
                .map(m -> new ChatMessageResponseDto(
                        m.getSender(),
                        m.getContent(),
                        m.getTimestamp().toString(),
                        m.getRoomId()
                ))
                .toList();
    }
}
