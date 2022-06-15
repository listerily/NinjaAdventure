package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.communication.MonsterData;
import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.communication.SceneData;
import net.listerily.NinjaAdventure.server.data.entities.Monster;

import java.util.UUID;

public class ServerMessageHandler {
    private final ClientDataManager clientDataManager;
    public ServerMessageHandler(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    public SCMessage handle(SCMessage message) {
        if (message.type == SCMessage.MSG_SERVER_HEARTBEAT) {
            return handleHeartbeatMessage();
        } else if (message.type == SCMessage.MSG_UPDATE_PLAYER_DATA) {
            return handlePlayerDataUpdateMessage((PlayerData) message.obj);
        } else if (message.type == SCMessage.MSG_UPDATE_SCENE_DATA) {
            return handleSceneDataUpdateMessage((SceneData) message.obj);
        } else if (message.type == SCMessage.MSG_SWITCH_SCENE) {
            return handleSwitchSceneMessage((UUID) message.obj);
        } else if (message.type == SCMessage.MSG_MONSTER_UPDATE) {
            return handleMonsterUpdateMessage((MonsterData) message.obj);
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

    private SCMessage handleMonsterUpdateMessage(MonsterData monsterData) {
        clientDataManager.updateMonsterData(monsterData);
        return null;
    }

    private SCMessage handleSwitchSceneMessage(UUID newSceneUUID) {
        clientDataManager.switchScene(newSceneUUID);
        return null;
    }


    private SCMessage handleSceneDataUpdateMessage(SceneData sceneData) {
        clientDataManager.updateSceneData(sceneData);
        return null;
    }
}
