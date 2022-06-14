package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;
import java.util.UUID;

public class SceneData implements Serializable, Cloneable {
    public int id;
    public int width, height;
    public TileData[][] tileSheet;
    public MonsterData[] monsterData;
    public PlayerData[] playerData;

    @Override
    public SceneData clone() {
        try {
            SceneData clone = (SceneData) super.clone();
            clone.id = id;
            clone.width = width;
            clone.height = height;
            if (tileSheet != null)
                clone.tileSheet = tileSheet.clone();
            if (monsterData != null)
                clone.monsterData = monsterData.clone();
            if (playerData != null)
                clone.playerData = playerData.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}