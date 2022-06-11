package net.listerily.NinjaAdventure.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    public GameServer() {

    }

    public void startService() {
        try {
            serverSocket = new ServerSocket(2022);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminateService() {

    }
}
