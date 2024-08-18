package org.studnia;

import jakarta.websocket.DeploymentException;
import org.glassfish.tyrus.server.Server;
import org.studnia.endpoints.SocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8080, "/", null, SocketHandler.class);
        try {
            server.start();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();

        }
    }
}