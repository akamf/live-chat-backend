package com.example.live_chat_backend.service;

import com.example.live_chat_backend.dto.LoginRequestDto;
import com.example.live_chat_backend.dto.SettingsDto;
import com.example.live_chat_backend.entity.SettingsConfig;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.repository.SettingsConfigRepository;
import com.example.live_chat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final SettingsConfigRepository settingsRepo;

    public User findById(String userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User createOrUpdateUser(LoginRequestDto request) {
        Optional<User> existing = repository.findByEmail(request.email());
        if (existing.isPresent()) {
            User user = existing.get();
            user.setLastLogin(LocalDateTime.now());
            user.setOnline(true);
            return repository.save(user);
        }

        User newUser = request.toUser();

        return repository.save(newUser);
    }

    public void logoutUser(String userId) {
        repository.findById(userId).ifPresent(user -> {
            user.setOnline(false);
            repository.save(user);
        });
    }

    public User updateUserName(String userId, String newName) {
        User user = findById(userId);
        user.setName(newName);
        return repository.save(user);
    }

    public SettingsDto getSettings(String userId) {
        User user = findById(userId);
        return settingsRepo.findByUser(user)
                .map(c -> new SettingsDto(c.getTextSize(), c.getDarkMode()))
                .orElse(new SettingsDto("medium", "system")); // fallback default
    }

    public void saveSettings(String userId, SettingsDto settingsDto) {
        User user = findById(userId);

        SettingsConfig config = settingsRepo.findByUser(user)
                .orElse(SettingsConfig.builder().user(user).build());

        config.setTextSize(settingsDto.textSize());
        config.setDarkMode(settingsDto.darkMode());

        settingsRepo.save(config);
    }
}
