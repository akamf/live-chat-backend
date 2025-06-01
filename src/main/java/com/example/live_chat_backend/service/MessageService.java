package com.example.live_chat_backend.service;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import com.example.live_chat_backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public void saveMessage(ChatMessageRequestDto message) {
        String timestamp = message.timestamp() != null
                ? message.timestamp()
                : Instant.now().toString();

        ChatMessageRequestDto updated = new ChatMessageRequestDto(
                message.sender(),
                message.content(),
                timestamp
        );

        repository.save(updated);
    }

    public List<ChatMessageRequestDto> getRecentMessages(int limit) {
        return repository.findRecent(limit);
    }
}
