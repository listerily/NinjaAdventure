package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;

public class GameFrame extends AppBaseFrame {
    public GameFrame(App app) {
        super(app);
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return true;
    }
}
