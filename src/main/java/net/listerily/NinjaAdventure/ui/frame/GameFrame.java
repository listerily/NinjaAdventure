package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.ui.components.GamingPanel;
import net.listerily.NinjaAdventure.ui.components.LoadingBar;
import net.listerily.NinjaAdventure.ui.components.LoadingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends AppBaseFrame {
    private final GamingPanel gamingPanel;
    private boolean initialized = false;
    public GameFrame(App app) {
        super(app);

//        LoadingPanel panel = new LoadingPanel(this);
//        add(panel);

        gamingPanel = new GamingPanel();
        add(gamingPanel);
    }

    private void initialize() {
        if (initialized)
            return;
        this.initialized = true;
        gamingPanel.initialize();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) initialize();
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return true;
    }
}
