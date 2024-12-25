package org.example.projet4dx.controller.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.Collections;

import static org.example.projet4dx.util.AuthenticationUtil.CURRENT_PLAYER_DTO;

/**
 * WebSocketConfig class extends ServerEndpointConfig.Configurator and provides customization for WebSocket handshake process.
 */
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            sec.getUserProperties().put("player", httpSession.getAttribute(CURRENT_PLAYER_DTO));
            sec.getUserProperties().put("session", httpSession);
        }
        response.getHeaders().put("Access-Control-Allow-Origin", Collections.singletonList("*"));
    }

}
