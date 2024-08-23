package org.studnia.endpoints;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat")
public class SocketHandler {
    private Session session;
    private static final Set<SocketHandler> connections = new HashSet<SocketHandler>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        System.out.println("Connection opened");
        connections.add(this);
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
    public void onMessage(String message, Session session) {
        System.out.println(message);
    }
}
