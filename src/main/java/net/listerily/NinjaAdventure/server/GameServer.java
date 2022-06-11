package net.listerily.NinjaAdventure.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    public GameServer() {
    }

    public void startService(int port) throws IOException {
        serverSocket = new ServerSocket(2022);

    }

    public void terminateService() throws IOException {
        if (serverSocket != null)
            serverSocket.close();
    }
}
