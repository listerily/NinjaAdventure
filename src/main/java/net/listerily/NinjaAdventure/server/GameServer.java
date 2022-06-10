package net.listerily.NinjaAdventure.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public GameServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(9930);
//            serverSocket.
            while (true) {
                Socket socket = serverSocket.accept();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startService() {

    }

    public void terminateService() {

    }
}
