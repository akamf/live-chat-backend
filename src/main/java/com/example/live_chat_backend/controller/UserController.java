package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.SettingsDto;
import com.example.live_chat_backend.dto.UpdateUserRequestDto;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUserName(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateUserRequestDto request
    ) {
        String userId = jwt.getSubject();
        User updatedUser = userService.updateUserName(userId, request.name());
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/settings")
    public SettingsDto getSettings(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return userService.getSettings(userId);
    }

    @PutMapping("/settings")
    public void saveSettings(@RequestBody SettingsDto dto, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.info("Saving settings for user {}", userId);
        userService.saveSettings(userId, dto);
    }
}
