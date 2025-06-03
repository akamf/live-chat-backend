package com.example.live_chat_backend.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WebSocketSessionRegistry {
    private static final int MAX_USERS_PER_ROOM = 8; // TODO: Make this dynamic
    private final ConcurrentHashMap<Long, Set<String>> sessionsPerRoom = new ConcurrentHashMap<>();

    public boolean tryAddUser(Long roomId, String userId) {
        sessionsPerRoom.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        Set<String> users = sessionsPerRoom.get(roomId);

        synchronized (users) {
            if (users.size() >= MAX_USERS_PER_ROOM) {
                return false;
            }
            return users.add(userId);
        }
    }

    public void removeUser(String roomId, String userId) {
        Set<String> users = sessionsPerRoom.get(roomId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                sessionsPerRoom.remove(roomId);
            }
        }
    }

    public Set<String> getUsersInRoom(Long roomId) {
        return sessionsPerRoom.getOrDefault(roomId, Set.of());
    }

    public Map<Long, Integer> getOnlineCounts() {
        return sessionsPerRoom.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()
                ));
    }
}
