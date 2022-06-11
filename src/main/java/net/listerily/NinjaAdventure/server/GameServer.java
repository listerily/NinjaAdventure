package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.communication.SCMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Level;

public class GameServer {
    private ServerSocket serverSocket;
    private HashMap<UUID, LinkedBlockingQueue<SCMessage>> messageQueueLookup;
    private HashMap<UUID, Thread> messageProducerLookup;
    private HashMap<UUID, Thread> messageConsumerLookup;
    private HashMap<UUID, Socket> socketLookup;

    private ExecutorService clientMessageExecutor;
    private ClientMessageHandler clientMessageHandler;
    private ServerDataManager serverDataManager;
    private static final int MAX_CLIENTS = 4;

    private int aliveConnections;
    private final App app;

    public GameServer(App app) {
        this.app = app;
    }

    public void startService(int port) throws IOException {
        synchronized (this) {
            this.serverSocket = new ServerSocket(port);
            this.clientMessageExecutor = Executors.newFixedThreadPool(4);
            this.messageQueueLookup = new HashMap<>();
            this.socketLookup = new HashMap<>();
            this.messageConsumerLookup = new HashMap<>();
            this.messageProducerLookup = new HashMap<>();
            this.aliveConnections = 0;
            this.serverDataManager = new ServerDataManager();
            this.clientMessageHandler = new ClientMessageHandler(serverDataManager);
            this.serverDataManager.initialize();
        }
        startListening();
    }

    private void startListening() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        app.getAppLogger().log(Level.INFO, "SERVER: Connected to client socket.");
                        initializeClientService(clientSocket, UUID.randomUUID());
                    } catch (IOException e) {
                        app.getAppLogger().log(Level.INFO, "SERVER: Failed to connect client socket.");
                    }
                }
            }
        }.start();
    }

    public void submitMessage(UUID targetClient, SCMessage message) throws InvalidKeyException, InterruptedException {
        LinkedBlockingQueue<SCMessage> targetQueue;
        synchronized (this) {
            if (!messageQueueLookup.containsKey(targetClient))
                throw new InvalidKeyException("SERVER: Target client with UUID=" + targetClient.toString() + " NOT FOUND.");
            targetQueue = messageQueueLookup.get(targetClient);
        }
        targetQueue.put(message);
    }

    private synchronized void initializeClientService(Socket socket, UUID clientUUID) throws IOException {
        try {
            if (aliveConnections >= MAX_CLIENTS) {
                app.getAppLogger().log(Level.INFO, "SERVER: Connections count reached to maximum. Disconnecting this client.");
                socket.close();
                return;
            }
            LinkedBlockingQueue<SCMessage> messageQueue = new LinkedBlockingQueue<>();
            ++aliveConnections;
            socketLookup.put(clientUUID, socket);
            messageQueueLookup.put(clientUUID, messageQueue);

            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            outputStream.writeObject(new SCMessage(SCMessage.MSG_CONNECTED, clientUUID));
            outputStream.flush();
            // Message Producer
            Thread messageProducer = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        SCMessage message;
                        try {
                            message = messageQueue.take();
                            try {
                                outputStream.writeObject(message);
                                outputStream.flush();
                            } catch (IOException e) {
                                app.getAppLogger().log(Level.SEVERE, "SERVER: IO Error while sending message. Terminating Client.", e);
                                terminateClient(clientUUID);
                                return;
                            }
                        } catch (InterruptedException e) {
                            app.getAppLogger().log(Level.WARNING, "SERVER: Producer thread interrupted.", e);
                            return;
                        }
                    }
                }
            };
            // Message Consumer
            Thread messageConsumer = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            SCMessage message = (SCMessage) inputStream.readObject();
                            clientMessageExecutor.submit(() -> {
                                SCMessage msg = handleClientMessage(clientUUID, message);
                                if (msg != null) {
                                    try {
                                        messageQueue.put(msg);
                                    } catch (InterruptedException e) {
                                        app.getAppLogger().log(Level.WARNING, "SERVER: Message Queue interrupted.", e);
                                    }
                                }
                            });
                        } catch (IOException | ClassNotFoundException e) {
                            app.getAppLogger().log(Level.SEVERE, "SERVER: IO Error while reading message. Terminating Client.", e);
                            terminateClient(clientUUID);
                            return;
                        }
                    }
                }
            };
            messageProducerLookup.put(clientUUID, messageProducer);
            messageConsumerLookup.put(clientUUID, messageConsumer);
            messageProducer.start();
            messageConsumer.start();
        } catch(IOException e) {
            if (messageProducerLookup.containsKey(clientUUID)) {
                messageProducerLookup.get(clientUUID).interrupt();
                messageProducerLookup.remove(clientUUID);
            }
            if (messageConsumerLookup.containsKey(clientUUID)) {
                messageConsumerLookup.get(clientUUID).interrupt();
                messageConsumerLookup.remove(clientUUID);
            }
            messageQueueLookup.remove(clientUUID);
            socketLookup.remove(clientUUID, socket);
            --aliveConnections;
            throw e;
        }
    }

    private SCMessage handleClientMessage(UUID clientUUID, SCMessage clientMessage) {
        return clientMessageHandler.handle(clientUUID, clientMessage);
    }

    private synchronized void terminateClient(UUID clientUUID) {
        messageConsumerLookup.get(clientUUID).interrupt();
        messageConsumerLookup.remove(clientUUID);
        messageProducerLookup.get(clientUUID).interrupt();
        messageProducerLookup.remove(clientUUID);
        try (Socket socket = socketLookup.get(clientUUID)) {
            socketLookup.remove(clientUUID, socket);
            socket.close();
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "SERVER: Unable to close client socket. Skipping.", e);
        }
        serverListener.onTerminateClientService(clientUUID);
    }

    public synchronized void terminateService() throws IOException {
        clientMessageExecutor.shutdownNow();
        // shutdown producer and consumer executions
        for (UUID key : messageProducerLookup.keySet()) {
            messageProducerLookup.get(key).interrupt();
        }
        for (UUID key : messageConsumerLookup.keySet()) {
            messageConsumerLookup.get(key).interrupt();
        }
        // close client sockets
        for (UUID key : socketLookup.keySet()) {
            socketLookup.get(key).close();
            serverListener.onTerminateClientService(key);
        }
        // close server socket
        if (serverSocket != null)
            serverSocket.close();
    }

    private ServerListener serverListener = new ServerListener() {
        @Override
        public void onTerminateClientService(UUID uuid) {

        }
    };

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public interface ServerListener {
        void onTerminateClientService(UUID uuid);
    }
}
