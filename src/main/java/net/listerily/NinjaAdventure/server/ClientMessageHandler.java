package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.communication.SCMessage;

import java.util.UUID;

public class ClientMessageHandler {
    private final ServerDataManager serverDataManager;

    public ClientMessageHandler(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
    }

    public SCMessage handle(UUID clientUUID, SCMessage message) {
        return new SCMessage(SCMessage.MSG_UNDEFINED, "Hello from server.");
    }
}
