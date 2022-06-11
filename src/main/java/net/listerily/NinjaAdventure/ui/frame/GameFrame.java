package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.ui.components.ErrorPanel;
import net.listerily.NinjaAdventure.ui.components.GamingPanel;
import net.listerily.NinjaAdventure.ui.components.LoadingPanel;

import javax.swing.*;
import java.awt.*;

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
        showLoadingPanel("Starting");
    }

    public void showPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.add(panel);
        this.repaint();
    }

    public void showErrorPanel(String message) {
        showPanel(errorPanel);
        errorPanel.setErrorMessage(message);
    }

    public void showLoadingPanel(String message) {
        showPanel(loadingPanel);
        loadingPanel.setLoadingMessage(message);
    }

    public void showGamingPanel() {
        showPanel(gamingPanel);
        gamingPanel.initialize();
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
