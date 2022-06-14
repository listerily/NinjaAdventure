package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.communication.*;
import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.Tile;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.server.data.entities.Player;
import net.listerily.NinjaAdventure.server.data.layers.Layer;

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
        }
        return null;
    }

    private SCMessage handleClientHeartbeatMessage() {
        return new SCMessage(SCMessage.MSG_CLIENT_HEARTBEAT_RESPONSE);
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
            clientInitializationData.playerData = new PlayerData();
            clientInitializationData.playerData.position = player.getPosition();
            clientInitializationData.playerData.uuid = clientUUID;
            clientInitializationData.playerData.health = player.getHealth();
            clientInitializationData.playerData.nickname = player.getNickname();
            clientInitializationData.playerData.character = player.getCharacter();
            clientInitializationData.playerData.facing = player.getFacing();
            clientInitializationData.playerData.hurting = player.isHurting();
            clientInitializationData.playerData.actionState = player.getActionState();
            clientInitializationData.playerData.maxHealth = player.getMaxHealth();
            clientInitializationData.playerData.dead = player.isDead();

            Scene scene = player.getScene();
            HashMap<String, Layer> layers = scene.getLayers();
            Layer[] layerArray = new Layer[layers.size()];
            int currentIndex = 0;
            for (HashMap.Entry<String, Layer> entry : layers.entrySet()) {
                Layer layer = entry.getValue();
                layerArray[currentIndex++] = layer;
            }
            layerArray = Arrays.stream(layerArray).sorted(Comparator.comparingInt(Layer::getDisplayPriority))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll).toArray(layerArray);
            clientInitializationData.sceneData = new SceneData();
            clientInitializationData.sceneData.id = scene.getId();
            clientInitializationData.sceneData.width = scene.getWidth();
            clientInitializationData.sceneData.height = scene.getHeight();
            // TODO: player data and monster data
            // clientInitializationData.sceneData.playerData
            // clientInitializationData.sceneData.monsterData
            TileData[][] tileData = new TileData[scene.getWidth()][scene.getHeight()];
            for (int x = 0; x < scene.getWidth(); ++x) {
                for (int y = 0; y < scene.getHeight(); ++y) {
                    tileData[x][y] = new TileData();
                    ArrayList<String> tileStackUpper = new ArrayList<>();
                    ArrayList<String> tileStackLower = new ArrayList<>();
                    boolean ableToStep = true;
                    boolean ableToInteract = false;
                    for (Layer layer : layerArray) {
                        Tile tile = layer.getTile(x, y);
                        if (tile != null) {
                            if (layer.isLowerLayer())
                                tileStackLower.add(tile.getId());
                            else if (layer.isUpperLayer())
                                tileStackUpper.add(tile.getId());
                            if (layer.ableToInteract(x, y))
                                ableToInteract = true;
                            if (!layer.ableToStep(x, y))
                                ableToStep = false;
                        }
                    }
                    tileData[x][y].ableToInteract = ableToInteract;
                    tileData[x][y].ableToStep = ableToStep;
                    tileData[x][y].tileStackLower = new String[tileStackLower.size()];
                    tileData[x][y].tileStackLower = tileStackLower.toArray(tileData[x][y].tileStackLower);
                    tileData[x][y].tileStackUpper = new String[tileStackUpper.size()];
                    tileData[x][y].tileStackUpper = tileStackUpper.toArray(tileData[x][y].tileStackUpper);
                }
            }
            clientInitializationData.sceneData.tileSheet = tileData;
        }
        return new SCMessage(SCMessage.MSG_SERVER_RESPONSE_INIT, clientInitializationData);
    }
}
