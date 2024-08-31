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
        System.out.println(headers);
        String userId = headers.get("user_id").get(0);

        config.getUserProperties().put("user_id", userId);
        config.getUserProperties();
    }
}
