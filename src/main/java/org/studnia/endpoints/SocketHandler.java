package org.studnia.endpoints;
import org.json.*;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.studnia.messages.DTOs.MessageDTO;
import org.studnia.requests.MessageRequest;
import org.studnia.users.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@ServerEndpoint(value = "/chat", configurator = CustomEndpointConfigurator.class)
public class SocketHandler {
    private Session session;
    private static final HashMap<String, SocketHandler> connections = new HashMap<String, SocketHandler>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String userId = (String) session.getUserProperties().get("user_id");
        connections.put(userId, this);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        this.session.close();
        connections.remove(this);
        System.out.println("Connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error occured " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        MessageRequest request = new MessageRequest(message);
        MessageDTO messageDTO = request.toDTO();
        UserService userService = new UserService();
        ArrayList<String> users = userService.getUsersInChatRoom(messageDTO.chatRoomId);
        broadcast(users, this.session, messageDTO.message);
    }

    public void broadcast(ArrayList<String> users, Session senderSession, String message) throws IOException {
        for (String userId : users) {
            Session userSession = connections.get(userId).session;
            if (userSession.isOpen() && !userSession.equals(senderSession)) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
}
