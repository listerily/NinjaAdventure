package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.communication.*;
import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.Tile;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.server.data.entities.Player;
import net.listerily.NinjaAdventure.server.data.layers.Layer;

import java.lang.reflect.Array;
import java.util.*;

public class ClientMessageHandler {
    private final ServerDataManager serverDataManager;
    private World world;

    public ClientMessageHandler(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
    }

    public SCMessage handle(UUID clientUUID, SCMessage message) {
        if (message.type == SCMessage.MSG_CLIENT_REQUEST_INIT) {
            return handleClientInitializationRequest(clientUUID, message);
        } else if (message.type == SCMessage.MSG_CLIENT_HEARTBEAT) {
            return handleClientHeartbeatMessage();
        } else if (message.type == SCMessage.MSG_PLAYER_MOVE) {
            return handlePlayerMoveRequest(clientUUID, (PlayerMoveData) message.obj);
        }
        return null;
    }

    private SCMessage handleClientHeartbeatMessage() {
        return new SCMessage(SCMessage.MSG_CLIENT_HEARTBEAT_RESPONSE);
    }

    private SCMessage handlePlayerMoveRequest(UUID clientUUID, PlayerMoveData playerMoveData) {
        synchronized (serverDataManager.getWorld()) {
            world = serverDataManager.getWorld();
            Player player = world.findPlayer(clientUUID);
            player.walk(playerMoveData.dx, playerMoveData.dy);
            player.markUpdated();
        }
        PlayerData playerData = generatePlayerData(clientUUID);
        return new SCMessage(SCMessage.MSG_UPDATE_PLAYER_DATA, playerData.clone());
    }

    private SCMessage handleClientInitializationRequest(UUID clientUUID, SCMessage message) {
        ClientInitializationData clientInitializationData = new ClientInitializationData();
        PlayerInfo playerInfo = (PlayerInfo) message.obj;
        synchronized (serverDataManager.getWorld()) {
            world = serverDataManager.getWorld();
            Player player = world.findPlayer(clientUUID);
            if (player == null) {
                throw new IllegalStateException("Unable to find player with UUID=" + clientUUID + ".");
            }
            player.setCharacter(playerInfo.character);
            player.setNickname(playerInfo.nickname);
            player.getScene().markUpdated();
            clientInitializationData.playerData = generatePlayerData(clientUUID);
            clientInitializationData.sceneData = SceneData.generateSceneData(world, player.getScene());
        }
        return new SCMessage(SCMessage.MSG_SERVER_RESPONSE_INIT, clientInitializationData.clone());
    }

    private PlayerData generatePlayerData(UUID clientUUID) {
        synchronized (serverDataManager.getWorld()) {
            world = serverDataManager.getWorld();
            Player player = world.findPlayer(clientUUID);
            return PlayerData.generatePlayerData(player).clone();
        }
    }
}
