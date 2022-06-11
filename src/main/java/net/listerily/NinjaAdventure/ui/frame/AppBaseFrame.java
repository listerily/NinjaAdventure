package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class AppBaseFrame extends JFrame {
    protected final App app;
    private final Font titleFont, textFont;
    public AppBaseFrame(App app) {
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

        setSize(1536, 1080);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (shouldExitOnWindowClose())
                    System.exit(0);
            }
        });
        setTitle("Ninja Adventure!");
    }

    protected boolean shouldExitOnWindowClose() {
        return false;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public Font getTextFont() {
        return textFont;
    }
}
