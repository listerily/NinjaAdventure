package net.listerily.NinjaAdventure.data;

import java.io.Serializable;

public class SCMessage implements Serializable {
    public int type;
    public final static int MSG_UNDEFINED = 0;
    public final static int MSG_CONNECTED = 1;
    public Object obj;

    public SCMessage(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public SCMessage(int type) {
        this(type, null);
    }
}
