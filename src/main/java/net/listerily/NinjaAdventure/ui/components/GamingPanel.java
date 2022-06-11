package net.listerily.NinjaAdventure.ui.components;

import javax.swing.*;
import java.awt.event.*;

public class GamingPanel extends JPanel {
    private GameCanvas canvas = null;

    public GamingPanel() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (canvas != null)
                    canvas.setSize(getSize());
            }
        });
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        canvas.setSize(getSize());
    }

    public void initialize() {
        canvas = new GameCanvas();
        this.add(canvas);
        canvas.setSize(getSize());
        canvas.createBufferStrategy(2);
    }
}
