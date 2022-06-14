package net.listerily.NinjaAdventure.communication;

import java.io.Serializable;

public class PlayerMoveData implements Serializable, Cloneable {
    public float dx;
    public float dy;

    @Override
    public PlayerMoveData clone() {
        try {
            PlayerMoveData clone = (PlayerMoveData) super.clone();
            clone.dx = dx;
            clone.dy = dy;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
