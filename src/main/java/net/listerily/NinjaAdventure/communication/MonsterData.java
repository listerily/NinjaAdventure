package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;

public class MonsterData implements Serializable, Cloneable {
    public Position position;
    public int health;
    public int maxHealth;

    @Override
    public MonsterData clone() {
        try {
            MonsterData clone = (MonsterData) super.clone();
            if (position != null)
                clone.position = position.clone();
            clone.health = health;
            clone.maxHealth = maxHealth;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
