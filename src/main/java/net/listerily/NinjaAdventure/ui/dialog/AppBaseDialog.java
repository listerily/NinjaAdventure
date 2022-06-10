package net.listerily.NinjaAdventure.ui.dialog;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class AppBaseDialog extends JDialog {
    protected final App app;
    private final Font titleFont, textFont;

    public AppBaseDialog(App app) {
        this.app = app;
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(app.getResourceManager().getResourceURL("icon.png"))));
        } catch (IOException | NullPointerException e) {
            app.getAppLogger().log(Level.SEVERE, "Unable to load app icon. Fallback to default icon. ", e);
        }
        ResourceManager resourceManager = app.getResourceManager();
        CachedResources cachedResources = resourceManager.getCachedResources();
        try {
            titleFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/ka1.ttf");
            textFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/Gameplay.ttf");
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        setSize(800, 600);
    }

    protected Font getTitleFont() {
        return titleFont;
    }

    protected Font getTextFont() {
        return textFont;
    }
}
