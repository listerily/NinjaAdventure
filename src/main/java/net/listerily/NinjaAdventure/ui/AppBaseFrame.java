package net.listerily.NinjaAdventure.ui;

import net.listerily.NinjaAdventure.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class AppBaseFrame extends JFrame {
    protected final App app;
    private long animationTick = 0L;
    public AppBaseFrame(App app) {
        this.app = app;
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(app.getResourceManager().getResourceURL("icon.png"))));
        } catch (IOException | NullPointerException e) {
            app.getAppLogger().log(Level.SEVERE, "Unable to load app icon. Fallback to default icon. ", e);
        }
        setSize(1536, 1080);
        if (requireTimedRepaint()) {
            new Timer(125, e -> {
                synchronized (this) { animationTick++; }
                repaint(10);
            }).start();
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (shouldExitOnWindowClose())
                    System.exit(0);
            }
        });
        setTitle("Ninja Adventure!");
    }

    protected boolean requireTimedRepaint() {
        return false;
    }

    protected boolean shouldExitOnWindowClose() {
        return false;
    }

    protected final synchronized long getAnimationTick() {
        return animationTick;
    }
}
