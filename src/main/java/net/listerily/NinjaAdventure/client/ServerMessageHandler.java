package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.communication.SCMessage;

public class ServerMessageHandler {
    private ClientDataManager clientDataManager;
    public ServerMessageHandler(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    public SCMessage handle(SCMessage message) {
        if (message.type == SCMessage.MSG_SERVER_HEARTBEAT) {
            return handleHeartbeatMessage();
        }
        return null;
    }

    private SCMessage handleHeartbeatMessage() {
        return new SCMessage(SCMessage.MSG_SERVER_HEARTBEAT_RESPONSE);
    }
}
