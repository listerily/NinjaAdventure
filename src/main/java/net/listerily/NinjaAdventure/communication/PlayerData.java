package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements Serializable, Cloneable {
    public UUID uuid;
    public String nickname;
    public String character;
    public int actionState;
    public boolean dead;
    public boolean hurting;
    public int facing;
    public Position position;
    public int health;
    public int maxHealth;

    @Override
    public PlayerData clone() {
        try {
            PlayerData clone = (PlayerData) super.clone();
            clone.uuid = this.uuid;
            clone.nickname = this.nickname;
            clone.character = this.character;
            clone.actionState = this.actionState;
            clone.dead = this.dead;
            clone.facing = this.facing;
            clone.hurting = this.hurting;
            if (this.position != null)
                clone.position = this.position.clone();
            clone.health = this.health;
            clone.maxHealth = this.maxHealth;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}