package net.listerily.NinjaAdventure;

import net.listerily.NinjaAdventure.client.ClientController;
import net.listerily.NinjaAdventure.client.ClientDataManager;
import net.listerily.NinjaAdventure.client.GameClient;
import net.listerily.NinjaAdventure.server.GameServer;

import java.io.IOException;
import java.util.logging.Level;

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
        if (isGameRunning())
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        gameThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    app.getAppLogger().log(Level.INFO, "Starting game as server.");
                    app.getAppLogger().log(Level.INFO, "Starting server service.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_STARTING_SERVER));
                    gameServer.startService(port);
                    app.getAppLogger().log(Level.INFO, "Server service started.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_STARTED_SERVER));
                    app.getAppLogger().log(Level.INFO, "Connecting client to server.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTING));
                    gameClient.connect("127.0.0.1", port);
                    app.getAppLogger().log(Level.INFO, "Client connected.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTED));
                    app.getAppLogger().log(Level.INFO, "Initializing client.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_INITIALIZING_CLIENT));
                    gameClient.initialize();
                    app.getAppLogger().log(Level.INFO, "Initialized client.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_INITIALIZED_CLIENT));
                    status = STATUS_SERVER;
                    app.getAppLogger().log(Level.INFO, "Game Launched.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_SUCCEED));
                } catch (Exception e) {
                    app.getAppLogger().log(Level.SEVERE, "Failed to start game as server. ", e);
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_FAILED, e));
                }
            }
        };
        gameThread.start();
    }

    public void startGameAsClient(String address, int port) {
        if (isGameRunning())
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        gameThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    app.getAppLogger().log(Level.INFO, "Starting game as client.");
                    app.getAppLogger().log(Level.INFO, "Connecting client to server.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTING));
                    gameClient.connect(address, port);
                    app.getAppLogger().log(Level.INFO, "Client connected.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_CONNECTED));
                    app.getAppLogger().log(Level.INFO, "Initializing client.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_INITIALIZING_CLIENT));
                    gameClient.initialize();
                    app.getAppLogger().log(Level.INFO, "Initialized client.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_INITIALIZED_CLIENT));
                    status = STATUS_CLIENT;
                    app.getAppLogger().log(Level.INFO, "Game Launched.");
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_SUCCEED));
                } catch (Exception  e) {
                    app.getAppLogger().log(Level.SEVERE, "Failed to start game as client. ", e);
                    gameStateListener.onEvent(new GameLaunchEvent(GameLaunchEvent.EVENT_FAILED, e));
                }
            }
        };
        gameThread.start();
    }

    public void terminateGame() {
        app.getAppLogger().log(Level.INFO, "Terminating game.");
        app.getAppLogger().log(Level.INFO, "Interrupting game launch thread.");
        gameThread.interrupt();
        app.getAppLogger().log(Level.INFO, "Terminating client service.");
        gameClient.terminateClient(new InterruptedException("Game interrupted by user."));
        app.getAppLogger().log(Level.INFO, "Terminating server service.");
        if (isHostingServer()) {
            try {
                gameServer.terminateService();
            } catch (IOException e) {
                app.getAppLogger().log(Level.WARNING, "IO Error while terminating server service. Ignored.", e);
            }
        }
        app.getAppLogger().log(Level.INFO, "Game terminated.");
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

    public ClientDataManager getClientDataManager() {
        return gameClient.getClientDataManager();
    }

    public ClientController getClientController() {
        return gameClient.getClientController();
    }
}
