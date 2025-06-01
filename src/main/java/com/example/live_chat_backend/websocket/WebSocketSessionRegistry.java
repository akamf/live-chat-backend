package com.example.live_chat_backend.websocket;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {
    private static final int MAX_USERS = 8; // TODO: Make this dynamic
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    public boolean tryAddUser(String userId) {
        if (activeUsers.size() >= MAX_USERS) return false;
        return activeUsers.add(userId);
    }

    public void removeUser(String userId) {
        activeUsers.remove(userId);
    }

    public Set<String> getActiveUsers() {
        return activeUsers;
    }

    public int getUserCount() {
        return activeUsers.size();
    }
}
