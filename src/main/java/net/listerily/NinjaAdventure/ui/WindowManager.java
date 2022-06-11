package net.listerily.NinjaAdventure.ui;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.ui.frame.GameFrame;
import net.listerily.NinjaAdventure.ui.dialog.JoinDialog;
import net.listerily.NinjaAdventure.ui.frame.MenuFrame;

public class WindowManager {
    private MenuFrame menuFrame;
    private GameFrame gameFrame;
    private final App app;

    public WindowManager(App app) {
        this.app = app;
        this.menuFrame = new MenuFrame(app);
        this.gameFrame = new GameFrame(app);
    }

    public void showMenuFrame() {
        this.menuFrame.setVisible(true);
    }

    public void hideMenuFrame() {
        this.menuFrame.setVisible(false);
    }

    public void showGameFrame(GameFrame.GameLaunchOptions options) {
        this.gameFrame.setVisible(true);
        this.gameFrame.launchGame(options);
    }

    public void hideGameFrame() {
        this.gameFrame.setVisible(false);
    }
}
