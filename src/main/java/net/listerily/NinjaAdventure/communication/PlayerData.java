package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.server.data.entities.Player;
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
    public int score;

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
            clone.score = this.score;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static PlayerData generatePlayerData(Player player) {
        PlayerData playerData = new PlayerData();
        playerData.position = player.getPosition();
        playerData.uuid = player.getUUID();
        playerData.health = player.getHealth();
        playerData.nickname = player.getNickname();
        playerData.character = player.getCharacter();
        playerData.facing = player.getFacing();
        playerData.hurting = player.isHurting();
        playerData.actionState = player.getActionState();
        playerData.maxHealth = player.getMaxHealth();
        playerData.dead = player.isDead();
        playerData.score = player.getScore();
        return playerData.clone();
    }
}