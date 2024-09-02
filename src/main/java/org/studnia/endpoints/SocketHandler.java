package org.studnia.endpoints;

import org.json.*;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.studnia.general.exceptions.MessageValidationException;
import org.studnia.messages.DTOs.MessageDTO;
import org.studnia.users.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@ServerEndpoint(value = "/chat", configurator = CustomEndpointConfigurator.class, decoders = MessageDecoder.class)
public class SocketHandler {
    private Session session;
    private String userId;
    private static final HashMap<String, SocketHandler> connections = new HashMap<String, SocketHandler>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Object userIdObj = session.getUserProperties().get("user_id");
        Object authToken = session.getUserProperties().get("token");

        if (authToken == null) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Authorization header is missing"));
            } catch (IOException e) {
                System.out.println("Failed to close the session due to missing Authorization header " + e.getMessage());
            }
        }

        if (userIdObj != null) {
            String userId = (String) userIdObj;
            connections.put(userId, this);
            this.userId = userId;
            System.out.println(userId + " connected");
        } else {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "'user_id' param is missing"));
            } catch (IOException e) {
                System.out.println("Failed to close the session due to missing 'user_id' param " + e.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        this.session.close();
        connections.remove(userId);
        System.out.println("Connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        if (throwable instanceof MessageValidationException) {
            System.out.println(throwable.getMessage());
            try {
                handleError(session, throwable, "Error: Invalid message - ");
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            try {
                handleError(session, throwable, "An error has occurred - ");
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void handleError(Session session, Throwable throwable, String message) throws IOException {
        if (session.isOpen()) {
            session.getBasicRemote().sendText(message + throwable.getMessage());
            System.out.println(message + session.getId());
        } else {
            System.out.println(message + session.getId());
        }
    }

    @OnMessage
    public void onMessage(MessageDTO messageDTO, Session session) throws IOException {
        System.out.println("Message received: " + messageDTO.message);
        UserService userService = new UserService();
        ArrayList<String> users = userService.getUsersInChatRoom(messageDTO.chatRoomId);
        broadcast(users, this.session, messageDTO.message);
    }

    public void broadcast(ArrayList<String> users, Session senderSession, String message) throws IOException {
        for (String userId : users) {
            Session userSession = connections.get(userId).session;
            if (userSession.isOpen() && !userSession.equals(senderSession)) {
                userSession.getBasicRemote().sendText(message);
            }
        }
    }
}
