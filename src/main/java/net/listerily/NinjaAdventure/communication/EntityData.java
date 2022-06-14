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

    public EntityData() {

    }

    public EntityData(EntityData entityData) {
        this.actionState = entityData.actionState;
        this.dead = entityData.dead;
        this.facing = entityData.facing;
        this.hurting = entityData.hurting;
        if (entityData.position != null)
            this.position = entityData.position.clone();
        this.health = entityData.health;
        this.maxHealth = entityData.maxHealth;
    }

    @Override
    public EntityData clone() {
        return new EntityData(this);
    }
}
