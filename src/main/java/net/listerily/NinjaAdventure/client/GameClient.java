package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.communication.SCMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

public class GameClient {
    private final App app;
    private Socket socket;
    private UUID uuid;
    private Thread clientListeningThread;
    private Thread clientWritingThread;
    private ClientDataManager clientDataManager;
    private ServerMessageHandler serverMessageHandler;
    private LinkedBlockingQueue<SCMessage> messageQueue;
    private ExecutorService clientMessageExecutor;

    public GameClient(App app) {
        this.app = app;
    }

    public void connect(String address, int port) throws IOException {
        socket = new Socket();
        socket.setKeepAlive(true);
        socket.connect(new InetSocketAddress(address, port));
    }

    public void startListening() {
        ObjectOutputStream outputStream;
        ObjectInputStream inputStream;
        try {
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.flush();
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            SCMessage msg = (SCMessage) inputStream.readObject();
            if (msg.type != SCMessage.MSG_CONNECTED) {
                throw new IOException("CLIENT: Could not read client UUID from server.");
            }
            uuid = (UUID)msg.obj;
            app.getAppLogger().log(Level.INFO, "CLIENT: Connected to server. UUID=" + uuid + ".");
            messageQueue = new LinkedBlockingQueue<>();
            clientMessageExecutor = Executors.newFixedThreadPool(1);
            clientDataManager = new ClientDataManager();
            serverMessageHandler = new ServerMessageHandler(clientDataManager);
        } catch (IOException | ClassNotFoundException e) {
            app.getAppLogger().log(Level.SEVERE, "CLIENT: IO Error while reading / writing message. Terminating myself.", e);
            terminateClient();
            return;
        }
        clientListeningThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        SCMessage message = (SCMessage) inputStream.readObject();
                        clientMessageExecutor.submit(() -> {
                            SCMessage msg = handleMessage(message);
                            if (msg != null) {
                                try {
                                    messageQueue.put(msg);
                                } catch (InterruptedException e) {
                                    app.getAppLogger().log(Level.WARNING, "CLIENT: Message Queue interrupted.", e);
                                }
                            }
                        });
                    } catch (IOException | ClassNotFoundException e) {
                        app.getAppLogger().log(Level.SEVERE, "CLIENT: IO Error while reading message. Terminating Client.", e);
                        terminateClient();
                        return;
                    }
                }
            }
        };
        clientWritingThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        SCMessage message = messageQueue.take();
                        try {
                            outputStream.writeObject(message);
                            outputStream.flush();
                        } catch (IOException e) {
                            app.getAppLogger().log(Level.SEVERE, "CLIENT: IO Error while writing message. Terminating myself.", e);
                            terminateClient();
                            return;
                        }
                    } catch (InterruptedException e) {
                        app.getAppLogger().log(Level.WARNING, "CLIENT: Write thread interrupted.", e);
                        return;
                    }
                }
            }
        };
        clientListeningThread.start();
        clientWritingThread.start();
    }

    public void terminateClient() {
        clientListeningThread.interrupt();
        clientWritingThread.interrupt();
        clientMessageExecutor.shutdownNow();
        try {
            socket.close();
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "CLIENT: IO Error while terminating my socket. Skipping.", e);
        }
        clientListener.onConnectionLost();
    }

    private SCMessage handleMessage(SCMessage message) {
        return serverMessageHandler.handle(message);
    }

    public void submitMessage(SCMessage message) throws InterruptedException {
        messageQueue.put(message);
    }


    private ClientListener clientListener = new ClientListener() {
        @Override
        public void onConnectionLost() {

        }
    };
    public void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    public interface ClientListener {
        void onConnectionLost();
    }
}
