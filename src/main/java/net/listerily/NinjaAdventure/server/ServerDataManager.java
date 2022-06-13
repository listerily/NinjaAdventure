package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.server.data.World;

import java.util.UUID;

public class ServerDataManager {
    private App app;
    private World world;
    public ServerDataManager(App app) {
        this.app = app;
    }

    public synchronized void initialize() {
        world = new World(app.getResourceManager());
    }

    public synchronized void onClientConnected(UUID clientUUID) {

    }

    public synchronized void onClientDisconnected(UUID clientUUID) {

    }

    public synchronized void tick(TickMessageHandler handler)  {
        if (world != null)
            world.tick(handler);
    }
}
