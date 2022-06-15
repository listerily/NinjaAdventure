package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.communication.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class ClientDataManager {
    private final App app;
    private SceneData currentSceneData;
    private PlayerData selfPlayer;
    private UUID currentSceneUUID;

    public ClientDataManager(App app) {
        this.app = app;
    }

    public void initialize(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.character = app.getOptionsManager().getCharacter();
        playerInfo.nickname = app.getOptionsManager().getNickname();
        outputStream.writeObject(new SCMessage(SCMessage.MSG_CLIENT_REQUEST_INIT, playerInfo));
        outputStream.flush();
        SCMessage response;
        do {
            response = (SCMessage) inputStream.readObject();
        } while (response.type != SCMessage.MSG_SERVER_RESPONSE_INIT);
        ClientInitializationData initializationData = (ClientInitializationData) response.obj;
        synchronized (this) {
            selfPlayer = initializationData.playerData;
            currentSceneData = initializationData.sceneData;
        }
    }

    public synchronized SceneData getCurrentSceneDataClone() {
        return currentSceneData.clone();
    }

    public synchronized PlayerData getSelfPlayerClone() {
        return selfPlayer.clone();
    }

    public synchronized void switchScene(UUID newSceneUUID) {
        currentSceneUUID = newSceneUUID;
    }

    public synchronized void updatePlayerData(PlayerData playerData) {
        if (playerData.uuid == selfPlayer.uuid) {
            this.selfPlayer = playerData.clone();
        } else {
            for (int i = 0; i < currentSceneData.playerData.length; ++i) {
                if (currentSceneData.playerData[i].uuid == playerData.uuid) {
                    currentSceneData.playerData[i] = playerData.clone();
                }
            }
        }
    }

    public synchronized void updateMonsterData(MonsterData monsterData) {
        for (int i = 0; i < currentSceneData.monsterData.length; ++i) {
            if (currentSceneData.monsterData[i].uuid == monsterData.uuid) {
                currentSceneData.monsterData[i] = monsterData.clone();
            }
        }
    }

    public synchronized void updateSceneData(SceneData sceneData) {
        if (currentSceneUUID == null || currentSceneUUID == sceneData.sceneUUID) {
            currentSceneUUID = sceneData.sceneUUID;
            this.currentSceneData = sceneData.clone();
        }
    }
}
