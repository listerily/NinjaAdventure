package net.listerily.NinjaAdventure.communication;

import java.io.Serializable;

public class TileData implements Serializable, Cloneable {
    public String[] tileStackLower;
    public String[] tileStackUpper;
    public boolean ableToStep;
    public boolean ableToInteract;

    @Override
    public TileData clone() {
        try {
            TileData clone = (TileData) super.clone();
            if (tileStackLower != null)
                clone.tileStackLower = tileStackLower.clone();
            if (tileStackUpper != null)
                clone.tileStackUpper = tileStackUpper.clone();
            clone.ableToStep = ableToStep;
            clone.ableToInteract = ableToInteract;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
