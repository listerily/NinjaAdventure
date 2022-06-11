package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.data.SCMessage;

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
    private ExecutorService clientMessageHandler;
    private static final int MAX_CLIENTS = 1;
    private int aliveConnections;
    private final App app;

    public GameServer(App app) {
        this.app = app;
    }

    public void startService(int port) throws IOException {
        synchronized (this) {
            this.serverSocket = new ServerSocket(port);
            this.clientMessageHandler = Executors.newFixedThreadPool(4);
            this.messageQueueLookup = new HashMap<>();
            this.socketLookup = new HashMap<>();
            this.messageConsumerLookup = new HashMap<>();
            this.messageProducerLookup = new HashMap<>();
            this.aliveConnections = 0;
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
                            clientMessageHandler.submit(() -> {
                                SCMessage msg = handleClientMessage(clientUUID, message);
                                try {
                                    messageQueue.put(msg);
                                } catch (InterruptedException e) {
                                    app.getAppLogger().log(Level.WARNING, "SERVER: Message Queue interrupted.", e);
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
        return null;
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
    }

    public synchronized void terminateService() throws IOException {
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
        }
        // close server socket
        if (serverSocket != null)
            serverSocket.close();
    }
}
