package net.listerily.NinjaAdventure.ui.components;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;

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

    public void initialize(App app, ClientDataManager clientDataManager) {
        canvas = new GameCanvas(app, clientDataManager);
        this.add(canvas);
        canvas.createBufferStrategy(2);
    }
}
