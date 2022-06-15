package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.server.data.entities.Monster;
import net.listerily.NinjaAdventure.server.data.entities.Player;
import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;
import java.util.UUID;

public class MonsterData implements Serializable, Cloneable {
    public UUID uuid;
    public int actionState;
    public boolean dead;
    public boolean hurting;
    public int facing;
    public Position position;
    public int health;
    public int maxHealth;
    public int type;
    @Override
    public MonsterData clone() {
        try {
            MonsterData clone = (MonsterData) super.clone();
            clone.uuid = this.uuid;
            clone.actionState = this.actionState;
            clone.dead = this.dead;
            clone.facing = this.facing;
            clone.hurting = this.hurting;
            clone.type = this.type;
            if (this.position != null)
                clone.position = this.position.clone();
            clone.health = this.health;
            clone.maxHealth = this.maxHealth;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static MonsterData generateMonsterData(Monster monster) {
        MonsterData monsterData = new MonsterData();
        monsterData.position = monster.getPosition();
        monsterData.uuid = monster.getUUID();
        monsterData.health = monster.getHealth();
        monsterData.facing = monster.getFacing();
        monsterData.hurting = monster.isHurting();
        monsterData.actionState = monster.getActionState();
        monsterData.maxHealth = monster.getMaxHealth();
        monsterData.dead = monster.isDead();
        monsterData.type = monster.getType();
        return monsterData.clone();
    }

}
