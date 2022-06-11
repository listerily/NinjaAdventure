package net.listerily.NinjaAdventure.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas {
    private boolean repaintInProgress = false;
    Color[] back = {Color.YELLOW, Color.BLUE};
    int page = 0;
    public GameCanvas(JComponent component) {
        setIgnoreRepaint(true);
        new Timer(20, e -> paintEvent()).start();
    }

    public void paintGraphics(Graphics graphics) {
        Dimension size = getSize();
        page++;
        page %= 2;
        graphics.setColor(back[page]);
        graphics.fillRect(0, 0, size.width, size.height);
        graphics.fillRect(0, 0, 900, 800);
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
