package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.communication.SCMessage;

public class ServerMessageHandler {
    private ClientDataManager clientDataManager;
    public ServerMessageHandler(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    public SCMessage handle(SCMessage message) {
        return new SCMessage(SCMessage.MSG_UNDEFINED, "Hello from client.");
    }
}
