package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;

public class EntityData implements Serializable, Cloneable {
    public int actionState;
    public boolean dead;
    public boolean hurting;
    public int facing;
    public Position position;
    public int health;
    public int maxHealth;

    @Override
    public EntityData clone() {
        try {
            EntityData clone = (EntityData) super.clone();
            clone.actionState = this.actionState;
            clone.dead = this.dead;
            clone.facing = this.facing;
            clone.hurting = this.hurting;
            if (clone.position != null)
                clone.position = this.position.clone();
            clone.health = this.health;
            clone.maxHealth = this.maxHealth;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
