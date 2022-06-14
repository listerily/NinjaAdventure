package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;

public class MonsterData implements Serializable, Cloneable {

    @Override
    public MonsterData clone() {
        try {
            return (MonsterData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
