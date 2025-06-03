package com.example.live_chat_backend.controller;

import com.example.live_chat_backend.dto.LoginRequestDto;
import com.example.live_chat_backend.entity.User;
import com.example.live_chat_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequestDto request) {
        User savedUser = userService.createOrUpdateUser(request);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        userService.logoutUser(userId);
        return ResponseEntity.ok().build();
    }
}
