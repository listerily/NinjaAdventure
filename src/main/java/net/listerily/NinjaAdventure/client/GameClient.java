package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.App;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
    private final App app;
    private Socket socket;

    public GameClient(App app) {
        this.app = app;
    }

    public void connect(String address, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(address, port));
    }

    public void loadData() {

    }

    public void startListening() {

    }
}
