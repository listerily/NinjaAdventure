package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.World;

public class Monster extends Entity {
    public Monster(World world) {
        super(world);
    }

    @Override
    public void tick(TickMessageHandler handler) {
        super.tick(handler);


    }

    @Override
    public int getMaxHealth() {
        return 4;
    }
}
