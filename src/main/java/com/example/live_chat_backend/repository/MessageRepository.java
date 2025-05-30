package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.dto.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepository {

    private final List<ChatMessage> messages = new ArrayList<>();

    public void save(ChatMessage message) {
        messages.add(message);
    }

    public List<ChatMessage> findRecent(int limit) {
        int fromIndex = Math.max(messages.size() - limit, 0);
        return new ArrayList<>(messages.subList(fromIndex, messages.size()));
    }

    public List<ChatMessage> findAll() {
        return new ArrayList<>(messages);
    }

    public void clear() {
        messages.clear();
    }
}
