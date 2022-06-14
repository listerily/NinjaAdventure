package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements Serializable, Cloneable {
    public Position position;
    public UUID uuid;
    public int health;

    @Override
    public PlayerData clone() {
        try {
            PlayerData clone = (PlayerData) super.clone();
            if (position != null)
                clone.position = position.clone();
            clone.uuid = uuid;
            clone.health = health;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}