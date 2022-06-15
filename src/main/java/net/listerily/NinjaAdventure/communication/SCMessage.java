package net.listerily.NinjaAdventure.communication;

import java.io.Serializable;

public class SCMessage implements Serializable {
    public int type;
    public final static int MSG_UNDEFINED = 0;
    public final static int MSG_CONNECTED = 1;
    public final static int MSG_CLIENT_REQUEST_INIT = 2;
    public final static int MSG_SERVER_RESPONSE_INIT = 3;
    public final static int MSG_SERVER_HEARTBEAT = 4;
    public final static int MSG_SERVER_HEARTBEAT_RESPONSE = 5;
    public final static int MSG_CLIENT_HEARTBEAT = 6;
    public final static int MSG_CLIENT_HEARTBEAT_RESPONSE = 7;
    public final static int MSG_PLAYER_MOVE = 8;
    public final static int MSG_UPDATE_PLAYER_DATA = 9;
    public final static int MSG_UPDATE_SCENE_DATA = 10;
    public final static int MSG_SWITCH_SCENE = 11;
    public final static int MSG_MONSTER_UPDATE = 12;
    public final static int MSG_PLAYER_ATTACK = 13;

    public Object obj;

    public SCMessage(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public SCMessage(int type) {
        this(type, null);
    }
}
