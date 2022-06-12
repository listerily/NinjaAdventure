package net.listerily.NinjaAdventure.rendering;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;

import java.awt.*;

public class Renderer {
    private App app;

    public Renderer(App app) {
        this.app = app;
    }

    public void drawGraphics(ClientDataManager clientDataManager, Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawRect(0, 0, 400, 400);
    }
}
