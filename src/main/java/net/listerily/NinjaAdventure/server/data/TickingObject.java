package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.server.TickMessageHandler;

public class TickingObject {
    protected boolean updated;
    public void markUpdated() {
        updated = true;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void tick(TickMessageHandler handler) {
        if (isUpdated()) {
            updated = false;
            yieldUpdateMessage(handler);
        }
    }

    public void yieldUpdateMessage(TickMessageHandler handler) {

    }

}
