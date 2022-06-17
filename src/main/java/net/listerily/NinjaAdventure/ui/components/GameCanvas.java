package net.listerily.NinjaAdventure.ui.components;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;
import net.listerily.NinjaAdventure.rendering.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;

public class GameCanvas extends Canvas {
    private boolean repaintInProgress = false;
    private final ClientDataManager clientDataManager;
    private final Renderer renderer;
    private Timer repaintTimer;
    public GameCanvas(App app, ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
        this.renderer = new Renderer(app);
        setIgnoreRepaint(true);
        setSize(1920, 1152);
        repaintTimer = new Timer(50, e -> {
            try {
                paintEvent();
            } catch (IllegalStateException exception) {
                app.getAppLogger().log(Level.WARNING, "Game canvas is destroyed. Terminating timer.", exception);
                repaintTimer.stop();
            }
        });
        repaintTimer.start();
        setFocusable(false);
    }

    public void paintGraphics(Graphics graphics) {
        Dimension size = getSize();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, size.width, size.height);
        renderer.drawGraphics(clientDataManager, graphics, size);
    }

    public synchronized void paintEvent() {
        if(repaintInProgress)
            return;
        repaintInProgress = true;
        BufferStrategy strategy = getBufferStrategy();
        Graphics graphics = strategy.getDrawGraphics();
        paintGraphics(graphics);
        graphics.dispose();
        strategy.show();
        Toolkit.getDefaultToolkit().sync();
        repaintInProgress = false;
    }
}
