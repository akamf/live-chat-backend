package com.example.live_chat_backend.exception;

public class WebSocketLimitException extends RuntimeException {
    public WebSocketLimitException(String message) {
        super(message);
    }
}