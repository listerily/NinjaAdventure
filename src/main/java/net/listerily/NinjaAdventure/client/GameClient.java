package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.data.SCMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private final App app;
    private Socket socket;
    private UUID uuid;

    public GameClient(App app) {
        this.app = app;
    }

    public void connect(String address, int port) throws IOException {
        socket = new Socket();
        socket.setKeepAlive(true);
        socket.connect(new InetSocketAddress(address, port));
    }

    public void loadData() throws IOException, ClassNotFoundException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outputStream.flush();
        ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        SCMessage msg = (SCMessage)inputStream.readObject();
        if (msg.type != SCMessage.MSG_CONNECTED) {
            throw new IOException("CLIENT: Could not read client UUID from server.");
        }
        this.uuid = (UUID)msg.obj;
        app.getAppLogger().log(Level.INFO, "CLIENT: Connected to server. UUID=" + uuid + ".");
    }

    public void startListening() {

    }
}
