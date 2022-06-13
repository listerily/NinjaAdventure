package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.data.World;

import java.util.UUID;

public class Player extends Entity {
    protected UUID playerUUID;
    public Player(World world, UUID playerUUID) {
        super(world);
        this.playerUUID = playerUUID;
        this.setHealth(getMaxHealth());
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public int getMaxHealth() {
        return 12;
    }

    public void moveToSpawn() {
        setPosition(getScene().getSpawnPosition());
    }
}
