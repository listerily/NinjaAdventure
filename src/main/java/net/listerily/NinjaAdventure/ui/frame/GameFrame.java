package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.GameLaunchEvent;
import net.listerily.NinjaAdventure.GameManager;
import net.listerily.NinjaAdventure.ui.components.ErrorPanel;
import net.listerily.NinjaAdventure.ui.components.GamingPanel;
import net.listerily.NinjaAdventure.ui.components.LoadingPanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

public class GameFrame extends AppBaseFrame {
    private final GamingPanel gamingPanel;
    private final ErrorPanel errorPanel;
    private final LoadingPanel loadingPanel;
    public GameFrame(App app) {
        super(app);

        setMinimumSize(new Dimension(1080, 720));
        loadingPanel = new LoadingPanel(this);
        errorPanel = new ErrorPanel(this);
        gamingPanel = new GamingPanel();
    }

    public void launchGame(GameLaunchOptions options) {
        showLoadingPanel("Starting Game");
        GameManager gameManager = app.getGameManager();
        if (options.hosting) {
            gameManager.setGameStateListener(event -> {
                if (event.type == GameLaunchEvent.EVENT_FAILED) {
                    showErrorPanel(event.obj.toString());
                } if (event.type == GameLaunchEvent.EVENT_SUCCEED) {
                    showGamingPanel();
                } else {
                    loadingPanel.setLoadingMessage(event.toString());
                }
            });
            gameManager.startGameAsServer(options.port);
        } else {
            gameManager.setGameStateListener(event -> {
                if (event.type == GameLaunchEvent.EVENT_FAILED) {
                    showErrorPanel(event.obj.toString());
                } if (event.type == GameLaunchEvent.EVENT_SUCCEED) {
                    showGamingPanel();
                } else {
                    loadingPanel.setLoadingMessage(event.toString());
                }
            });
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
        showPanel(gamingPanel);
        gamingPanel.initialize();
        this.repaint();
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return true;
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
