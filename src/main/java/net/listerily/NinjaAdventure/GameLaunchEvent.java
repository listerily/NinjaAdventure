package net.listerily.NinjaAdventure;

public class GameLaunchEvent {
    public int type;
    public Object obj;
    public static final int EVENT_UNDEFINED = 0;
    public static final int EVENT_FAILED = 1;
    public static final int EVENT_SUCCEED = 2;
    public static final int EVENT_STARTING_SERVER = 3;
    public static final int EVENT_STARTED_SERVER = 4;
    public static final int EVENT_CONNECTING = 5;
    public static final int EVENT_CONNECTED = 6;
    public static final int EVENT_LOADING_DATA = 7;
    public static final int EVENT_LOADED_DATA = 8;

    public GameLaunchEvent(int type) {
        this.type = type;
    }

    public GameLaunchEvent(int type, Object obj) {
        this(type);
        this.obj = obj;
    }

    @Override
    public String toString() {
        switch (type) {
            case EVENT_FAILED:
                return "Failed";
            case EVENT_CONNECTED:
                return "Connected to Server";
            case EVENT_CONNECTING:
                return "Connecting to Server";
            case EVENT_SUCCEED:
                return "Succeed";
            case EVENT_STARTING_SERVER:
                return "Starting Server";
            case EVENT_STARTED_SERVER:
                return "Started Server";
            case EVENT_LOADING_DATA:
                return "Loading Data";
            case EVENT_LOADED_DATA:
                return "Loaded Data";
            case EVENT_UNDEFINED:
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }
}
