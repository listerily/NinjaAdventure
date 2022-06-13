package net.listerily.NinjaAdventure.ui.components;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;
import net.listerily.NinjaAdventure.rendering.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas {
    private boolean repaintInProgress = false;
    private ClientDataManager clientDataManager;
    private Renderer renderer;
    public GameCanvas(App app, ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
        this.renderer = new Renderer(app);
        setIgnoreRepaint(true);
        setSize(1920, 1152);
        new Timer(20, e -> paintEvent()).start();
    }

    public void paintGraphics(Graphics graphics) {
        Dimension size = getSize();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, size.width, size.height);
        renderer.drawGraphics(clientDataManager, graphics);
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
