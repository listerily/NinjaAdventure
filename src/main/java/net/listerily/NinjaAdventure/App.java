package net.listerily.NinjaAdventure;

import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.ui.WindowManager;

import java.util.logging.Logger;

public class App {
    private final Logger logger;
    private final ResourceManager resourceManager;
    private final WindowManager windowManager;
    private final GameManager gameManager;

    public App() {
        logger = Logger.getLogger("NinjaAdventure");
        resourceManager = new ResourceManager(this);
        windowManager = new WindowManager(this);
        gameManager = new GameManager(this);
    }

    public void startGame() {
        windowManager.showMenuFrame();
    }

    public Logger getAppLogger() {
        return logger;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public WindowManager getWindowManager()  {
        return windowManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
