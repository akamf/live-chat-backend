package com.example.live_chat_backend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
public class RoomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        var servletRequest = (request instanceof ServletServerHttpRequest)
                ? ((ServletServerHttpRequest) request).getServletRequest()
                : null;

        if (servletRequest != null) {
            String roomId = servletRequest.getParameter("room-id");
            log.info("RoomId={}", roomId);
            attributes.put("room-id", roomId); // 👈 detta sätts korrekt
        }

        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception ex) {
        // No-op
    }

}
