package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.util.Position;

import java.io.Serializable;

public class ClientInitializationData implements Serializable, Cloneable {
    public PlayerData playerData;
    public SceneData sceneData;

    @Override
    public ClientInitializationData clone() {
        try {
            ClientInitializationData clone = (ClientInitializationData) super.clone();
            if (playerData != null)
                clone.playerData = playerData.clone();
            if (sceneData != null)
                clone.sceneData = sceneData.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
