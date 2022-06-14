package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.GameLaunchEvent;
import net.listerily.NinjaAdventure.GameManager;
import net.listerily.NinjaAdventure.client.GameClient;
import net.listerily.NinjaAdventure.ui.components.ErrorPanel;
import net.listerily.NinjaAdventure.ui.components.GamingPanel;
import net.listerily.NinjaAdventure.ui.components.LoadingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends AppBaseFrame {
    private GamingPanel gamingPanel;
    private ErrorPanel errorPanel;
    private LoadingPanel loadingPanel;
    public GameFrame(App app) {
        super(app);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                app.getGameManager().terminateGame();
                app.getWindowManager().showMenuFrame();
            }
        });
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
