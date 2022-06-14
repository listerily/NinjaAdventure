package net.listerily.NinjaAdventure.communication;

import java.io.Serializable;

public class PlayerInfo implements Serializable, Cloneable {
    public String nickname;
    public String character;
    
    @Override
    public PlayerInfo clone() {
        try {
            PlayerInfo clone = (PlayerInfo) super.clone();
            clone.nickname = nickname;
            clone.character = character;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
