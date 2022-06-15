package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.World;

import java.util.UUID;

public class Player extends Entity {
    protected String nickname;
    protected String character;
    public Player(World world, UUID playerUUID) {
        super(world, playerUUID);
        this.setHealth(getMaxHealth());
        this.nickname = "UNKNOWN";
        this.character = "BlueNinja";
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

    @Override
    public void yieldUpdateMessage(TickMessageHandler handler) {
        super.yieldUpdateMessage(handler);
        handler.submit(new SCMessage(SCMessage.MSG_UPDATE_PLAYER_DATA, PlayerData.generatePlayerData(this).clone()));
    }
}
