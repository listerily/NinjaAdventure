package net.listerily.NinjaAdventure;

import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.ui.MenuFrame;

import java.util.logging.Logger;

public class App {
    private final Logger logger;
    private final ResourceManager resourceManager;

    public App() {
        logger = Logger.getLogger("NinjaAdventure");
        resourceManager = new ResourceManager(this);
    }

    public void startGame() {
        new MenuFrame(this).setVisible(true);
    }

    public Logger getAppLogger() {
        return logger;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }
}
