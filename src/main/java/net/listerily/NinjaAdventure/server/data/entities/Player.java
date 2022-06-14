package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.data.World;

import java.util.UUID;

public class Player extends Entity {
    protected UUID playerUUID;
    protected String nickname;
    protected String character;
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

    public String getNickname() {
        return nickname;
    }

    public String getCharacter() {
        return character;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
