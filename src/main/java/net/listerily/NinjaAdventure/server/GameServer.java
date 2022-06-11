package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.App;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class GameServer {
    private ServerSocket serverSocket;
    private final App app;

    public GameServer(App app) {
        this.app = app;
    }

    public void startService(int port) throws IOException {
        serverSocket = new ServerSocket(2022);
        startListening();
    }

    public void startListening() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        app.getAppLogger().log(Level.INFO, "Connected to client socket.");
                    } catch (IOException e) {
                        app.getAppLogger().log(Level.INFO, "Failed to connect client socket.");
                    }
                }
            }
        }.start();
    }

    public void terminateService() throws IOException {
        if (serverSocket != null)
            serverSocket.close();
    }
}
