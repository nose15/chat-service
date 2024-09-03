package org.studnia.endpoints;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.HandshakeRequest;

import java.util.List;
import java.util.Map;

public class CustomEndpointConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(config, request, response);
        Map<String, List<String>> headers = request.getHeaders();
        Map<String, List<String>> params = request.getParameterMap();

        String authToken = null;
        String userId = null;
        if  (headers.containsKey("Authorization")) {
            authToken = headers.get("Authorization").get(0);
        }

        if  (params.containsKey("user_id")) {
            userId = params.get("user_id").get(0);
        }

        config.getUserProperties().put("token", authToken);
        config.getUserProperties().put("user_id", userId);
    }
}
