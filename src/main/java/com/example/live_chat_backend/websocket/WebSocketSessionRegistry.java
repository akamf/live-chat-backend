package com.example.live_chat_backend.websocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionRegistry {

    // Map<roomId, Set<userId>>
    private final Map<Long, Set<String>> activeUsers = new ConcurrentHashMap<>();

    public void register(Long roomId, String userId) {
        activeUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        log.debug("Registered user {} in room {}", userId, roomId);
    }

    public void unregister(Long roomId, String userId) {
        Set<String> users = activeUsers.get(roomId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) activeUsers.remove(roomId);
            log.debug("Unregistered user {} from room {}", userId, roomId);
        }
    }

    public Set<String> getUsersInRoom(Long roomId) {
        return activeUsers.getOrDefault(roomId, Set.of());
    }

    public Map<Long, Set<String>> getAll() {
        return activeUsers;
    }
}
