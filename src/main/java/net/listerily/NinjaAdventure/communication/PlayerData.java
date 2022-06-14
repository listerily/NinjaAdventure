package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData extends EntityData implements Serializable, Cloneable {
    public UUID uuid;
    public String nickname;
    public String character;

    public PlayerData() {

    }

    public PlayerData(PlayerData playerData) {
        super(playerData);
        this.uuid = playerData.uuid;
        this.nickname = playerData.nickname;
        this.character = playerData.character;
    }

    @Override
    public PlayerData clone() {
        return new PlayerData(this);
    }
}