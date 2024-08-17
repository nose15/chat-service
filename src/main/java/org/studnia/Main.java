package org.studnia;

import org.glassfish.tyrus.server.Server;
import org.studnia.endpoints.SocketHandler;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws DeploymentException {
        Server server = new Server("localhost", 8080, "/", SocketHandler.class);
        server.start();

        try {
            server.start();
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