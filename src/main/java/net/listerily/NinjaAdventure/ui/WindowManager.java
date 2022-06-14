package net.listerily.NinjaAdventure.ui;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.ui.frame.GameFrame;
import net.listerily.NinjaAdventure.ui.frame.MenuFrame;

public class WindowManager {
    private final MenuFrame menuFrame;
    private final GameFrame gameFrame;

    public WindowManager(App app) {
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
        this.gameFrame.initializeFrame();
        this.gameFrame.setVisible(true);
        this.gameFrame.launchGame(options);
    }

    public void hideGameFrame() {
        this.gameFrame.setVisible(false);
    }
}
