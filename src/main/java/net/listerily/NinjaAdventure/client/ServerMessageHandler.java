package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SCMessage;

public class ServerMessageHandler {
    private ClientDataManager clientDataManager;
    public ServerMessageHandler(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    public SCMessage handle(SCMessage message) {
        if (message.type == SCMessage.MSG_SERVER_HEARTBEAT) {
            return handleHeartbeatMessage();
        } else if (message.type == SCMessage.MSG_UPDATE_PLAYER_DATA) {
            return handlePlayerDataUpdateMessage((PlayerData) message.obj);
        }
        return null;
    }

    private SCMessage handleHeartbeatMessage() {
        return new SCMessage(SCMessage.MSG_SERVER_HEARTBEAT_RESPONSE);
    }

    private SCMessage handlePlayerDataUpdateMessage(PlayerData playerData) {
        clientDataManager.updatePlayerData(playerData);
        return null;
    }
}
