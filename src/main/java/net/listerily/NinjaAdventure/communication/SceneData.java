package net.listerily.NinjaAdventure.communication;

import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.Tile;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.server.data.entities.Player;
import net.listerily.NinjaAdventure.server.data.layers.Layer;

import java.io.Serializable;
import java.util.*;

public class SceneData implements Serializable, Cloneable {
    public int id;
    public int width, height;
    public TileData[][] tileSheet;
    public MonsterData[] monsterData;
    public PlayerData[] playerData;
    public UUID sceneUUID;

    @Override
    public SceneData clone() {
        try {
            SceneData clone = (SceneData) super.clone();
            clone.id = id;
            clone.width = width;
            clone.height = height;
            if (tileSheet != null) {
                clone.tileSheet = new TileData[width][height];
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        clone.tileSheet[x][y] = tileSheet[x][y].clone();
                    }
                }
            }
            if (monsterData != null) {
                clone.monsterData = new MonsterData[monsterData.length];
                for (int i = 0; i < monsterData.length; ++i) {
                    clone.monsterData[i] = monsterData[i].clone();
                }
            }

            if (playerData != null) {
                clone.playerData = new PlayerData[playerData.length];
                for (int i = 0; i < playerData.length; ++i) {
                    clone.playerData[i] = playerData[i].clone();
                }
            }
            clone.sceneUUID = sceneUUID;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static SceneData generateSceneData(World world, Scene scene) {
        SceneData sceneData = new SceneData();
        HashMap<String, Layer> layers = scene.getLayers();
        Layer[] layerArray = new Layer[layers.size()];
        int currentIndex = 0;
        for (HashMap.Entry<String, Layer> entry : layers.entrySet()) {
            Layer layer = entry.getValue();
            layerArray[currentIndex++] = layer;
        }
        layerArray = Arrays.stream(layerArray).sorted(Comparator.comparingInt(Layer::getDisplayPriority))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll).toArray(layerArray);
        sceneData.id = scene.getId();
        sceneData.width = scene.getWidth();
        sceneData.height = scene.getHeight();
        sceneData.sceneUUID = scene.getUUID();

        ArrayList<Player> players = world.getEntities().stream().filter(entity -> entity instanceof Player && entity.getScene() == scene)
                .collect(ArrayList::new, (arrayList, entity) -> arrayList.add((Player) entity), ArrayList::addAll);
        sceneData.playerData = new PlayerData[players.size()];
        currentIndex = 0;
        for (Player otherPlayer : players) {
            sceneData.playerData[currentIndex++] = PlayerData.generatePlayerData(otherPlayer).clone();
        }

        // TODO: monster data
        // sceneData.monsterData
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
        sceneData.tileSheet = tileData;
        return sceneData.clone();
    }
}