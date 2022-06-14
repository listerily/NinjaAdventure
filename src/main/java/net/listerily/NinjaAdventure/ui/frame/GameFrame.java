package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.GameLaunchEvent;
import net.listerily.NinjaAdventure.GameManager;
import net.listerily.NinjaAdventure.client.ClientController;
import net.listerily.NinjaAdventure.client.GameClient;
import net.listerily.NinjaAdventure.ui.components.ErrorPanel;
import net.listerily.NinjaAdventure.ui.components.GamingPanel;
import net.listerily.NinjaAdventure.ui.components.LoadingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameFrame extends AppBaseFrame {
    private GamingPanel gamingPanel;
    private ErrorPanel errorPanel;
    private LoadingPanel loadingPanel;
    private boolean isRunningGame;
    private final boolean[] orientationKeys = new boolean[4];
    private Timer controllerTimer;
    public GameFrame(App app) {
        super(app);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                app.getGameManager().terminateGame();
                app.getWindowManager().showMenuFrame();
                GameFrame.this.isRunningGame = false;
                if (controllerTimer != null)
                    controllerTimer.stop();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (orientationKeys) {
                    switch (e.getKeyCode()) {
                        case 40: case 83:
                            orientationKeys[0] = true;
                            break;
                        case 37: case 65:
                            orientationKeys[1] = true;
                            break;
                        case 38: case 87:
                            orientationKeys[2] = true;
                            break;
                        case 39: case 68:
                            orientationKeys[3] = true;
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (orientationKeys) {
                    switch (e.getKeyCode()) {
                        case 40: case 83:
                            orientationKeys[0] = false;
                            break;
                        case 37: case 65:
                            orientationKeys[1] = false;
                            break;
                        case 38: case 87:
                            orientationKeys[2] = false;
                            break;
                        case 39: case 68:
                            orientationKeys[3] = false;
                            break;
                    }
                }
            }
        });
        this.isRunningGame = false;
    }

    public void initializeFrame() {
        this.getContentPane().removeAll();
        setMinimumSize(new Dimension(1280, 768));
        loadingPanel = new LoadingPanel(this);
        errorPanel = new ErrorPanel(this);
        gamingPanel = new GamingPanel();
    }

    public void launchGame(GameLaunchOptions options) {
        showLoadingPanel("Starting Game");
        GameManager gameManager = app.getGameManager();
        gameManager.renewInstance();
        gameManager.setClientListener(new GameClient.ClientListener() {
            @Override
            public void onConnectionLost(Exception e) {
                showErrorPanel(e.toString());
                GameFrame.this.isRunningGame = false;
                if (controllerTimer != null)
                    controllerTimer.stop();
            }
        });
        gameManager.setGameStateListener(event -> {
            if (event.type == GameLaunchEvent.EVENT_FAILED) {
                showErrorPanel(event.obj.toString());
            } if (event.type == GameLaunchEvent.EVENT_SUCCEED) {
                showGamingPanel();
                if (gameManager.isClientOnly()) {
                    setTitle(getTitle() + " [CLIENT]");
                } else if (gameManager.isHostingServer()) {
                    setTitle(getTitle() + " [HOSTING: " + options.port + "]");
                }
                GameFrame.this.isRunningGame = true;
                controllerTimer = new Timer(50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isRunningGame) {
                            ClientController clientController = gameManager.getClientController();
                            synchronized (orientationKeys) {
                                clientController.move(orientationKeys[0], orientationKeys[1], orientationKeys[2], orientationKeys[3]);
                            }
                        }
                    }
                });
                controllerTimer.start();
            } else {
                loadingPanel.setLoadingMessage(event.toString());
            }
        });
        if (options.hosting) {
            gameManager.startGameAsServer(options.port);
        } else {
            gameManager.startGameAsClient(options.address, options.port);
        }
    }

    public void showPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.add(panel);
    }

    public void showErrorPanel(String message) {
        showPanel(errorPanel);
        errorPanel.setErrorMessage(message);
        this.repaint();
    }

    public void showLoadingPanel(String message) {
        showPanel(loadingPanel);
        loadingPanel.setLoadingMessage(message);
        this.repaint();
    }

    public void showGamingPanel() {
        GameManager gameManager = app.getGameManager();
        showPanel(gamingPanel);
        gamingPanel.initialize(app, gameManager.getClientDataManager());
        this.repaint();
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return false;
    }

    public static class GameLaunchOptions {
        public boolean hosting;
        public String address;
        public int port;

        public GameLaunchOptions(int port) {
            this.hosting = true;
            this.port = port;
        }

        public GameLaunchOptions(String address, int port) {
            this.hosting = false;
            this.port = port;
            this.address = address;
        }
    }
}
