package com.example.live_chat_backend.config;

import com.example.live_chat_backend.websocket.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class ApplicationConfig {


    @Bean
    public JwtHandshakeInterceptor jwtHandshakeInterceptor(JwtDecoder jwtDecoder) {
        return new JwtHandshakeInterceptor(jwtDecoder);
    }

}
