package com.example.live_chat_backend.repository;

import com.example.live_chat_backend.entity.SettingsConfig;
import com.example.live_chat_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsConfigRepository extends JpaRepository<SettingsConfig, String> {
    Optional<SettingsConfig> findByUser(User user);
}
