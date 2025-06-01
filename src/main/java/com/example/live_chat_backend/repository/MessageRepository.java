package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.dto.ChatMessageRequestDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepository {

    private final List<ChatMessageRequestDto> messages = new ArrayList<>();

    public void save(ChatMessageRequestDto message) {
        messages.add(message);
    }

    public List<ChatMessageRequestDto> findRecent(int limit) {
        int fromIndex = Math.max(messages.size() - limit, 0);
        return new ArrayList<>(messages.subList(fromIndex, messages.size()));
    }

    public List<ChatMessageRequestDto> findAll() {
        return new ArrayList<>(messages);
    }

    public void clear() {
        messages.clear();
    }
}
