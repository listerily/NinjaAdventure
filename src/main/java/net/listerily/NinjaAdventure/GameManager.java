package net.listerily.NinjaAdventure;

import net.listerily.NinjaAdventure.client.GameClient;
import net.listerily.NinjaAdventure.server.GameServer;

import java.io.IOException;

public class GameManager {
    private final App app;
    private int status = STATUS_NOT_RUNNING;
    private GameServer gameServer = null;
    private GameClient gameClient = null;
    private Thread gameThread;
    public static final int STATUS_NOT_RUNNING = 0;
    public static final int STATUS_SERVER = 1;
    public static final int STATUS_CLIENT = 2;

    public GameManager(App app) {
        this.app = app;
        gameServer = new GameServer(app);
        gameClient = new GameClient(app);
    }

    public void renewInstance() {
        gameServer = new GameServer(app);
        gameClient = new GameClient(app);
        status = STATUS_NOT_RUNNING;
    }

    public void startGameAsServer(int port) {
        if (status != STATUS_NOT_RUNNING)
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        gameThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_STARTING_SERVER));
                    gameServer.startService(port);
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_STARTED_SERVER));
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTING));
                    gameClient = new GameClient(app);
                    gameClient.connect("127.0.0.1", port);
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTED));
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_LOADING_DATA));
                    gameClient.startListening();
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_LOADED_DATA));
                    status = STATUS_SERVER;
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_SUCCEED));
                } catch (IOException e) {
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_FAILED, e));
                }
            }
        };
        gameThread.start();
    }

    public void startGameAsClient(String address, int port) {
        if (status != STATUS_NOT_RUNNING)
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        gameThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTING));
                    gameClient.connect(address, port);
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTED));
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_LOADING_DATA));
                    gameClient.startListening();
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_LOADED_DATA));
                    status = STATUS_CLIENT;
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_SUCCEED));
                } catch (IOException  e) {
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_FAILED, e));
                }
            }
        };
        gameThread.start();
    }

    public void terminateGame() {
        status = STATUS_NOT_RUNNING;
    }

    private GameStateListener gameStateListener = event -> {};
    public void setGameStateListener(GameStateListener gameStateListener) {
        this.gameStateListener = gameStateListener;
    }

    public int getGameStatus() {
        return status;
    }

    public boolean isGameRunning() {
        return getGameStatus() != STATUS_NOT_RUNNING;
    }

    public boolean isClientOnly() {
        return getGameStatus() == STATUS_CLIENT;
    }

    public boolean isHostingServer() {
        return getGameStatus() == STATUS_SERVER;
    }

    public void setClientListener(GameClient.ClientListener clientListener) {
        gameClient.setClientListener(clientListener);
    }
}
