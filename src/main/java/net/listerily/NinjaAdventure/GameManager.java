package net.listerily.NinjaAdventure;

import net.listerily.NinjaAdventure.client.GameClient;
import net.listerily.NinjaAdventure.server.GameServer;

import java.io.IOException;

public class GameManager {
    private final App app;
    private int status = STATUS_NOT_RUNNING;
    private GameServer gameServer = null;
    private GameClient gameClient = null;
    private GameStateListener gameStateListener = null;
    private Thread gameThread;
    public static final int STATUS_NOT_RUNNING = 0;
    public static final int STATUS_SERVER = 1;
    public static final int STATUS_CLIENT = 2;

    public GameManager(App app) {
        this.app = app;
    }

    public void startGameAsServer(int port) throws IOException {
        if (status != STATUS_NOT_RUNNING)
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        gameThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (gameStateListener != null) gameStateListener.onEvent(new GameEvent(GameEvent.EVENT_STARTING_SERVER));
                    gameServer = new GameServer();
                    gameServer.startService(port);
                    if (gameStateListener != null) gameStateListener.onEvent(new GameEvent(GameEvent.EVENT_STARTED_SERVER));
                    status = STATUS_SERVER;
                    if (gameStateListener != null) gameStateListener.onEvent(new GameEvent(GameEvent.EVENT_SUCCEED));
                } catch (Exception throwable) {
                    if (gameStateListener != null) gameStateListener.onEvent(new GameEvent(GameEvent.EVENT_FAILED, throwable));
                }
            }
        };
        gameThread.start();
    }

    public void startGameAsClient(String address, int port) {
        if (status != STATUS_NOT_RUNNING)
            throw new IllegalStateException("Game has been started. DO NOT start duplicate instances.");
        status = STATUS_CLIENT;
    }

    public void terminateGame() {
        status = STATUS_NOT_RUNNING;
    }

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
}
