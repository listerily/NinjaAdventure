package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;

public class MonsterData extends EntityData implements Serializable, Cloneable {
    public MonsterData() {

    }

    public MonsterData(MonsterData monsterData) {
        super(monsterData);
    }
    @Override
    public MonsterData clone() {
        return new MonsterData(this);
    }
}
