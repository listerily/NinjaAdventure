package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.communication.SceneData;
import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.layers.*;
import net.listerily.NinjaAdventure.util.Position;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Scene extends TickingObject {
    protected String name;
    protected Position spawnPosition;
    protected HashMap<String, Layer> layers;
    protected int id, width, height;
    protected final UUID uuid;
    protected final World world;
    public Scene(ResourceManager resourceManager, World world, int id) {
        uuid = UUID.randomUUID();
        this.world = world;
        try {
            JSONArray scenes = new JSONArray(resourceManager.getCachedResources().readText("Scenes/scene.json"));
            JSONObject sceneInfo = scenes.getJSONObject(id);
            this.name = sceneInfo.getString("name");
            JSONObject sceneObject = new JSONObject(resourceManager.getCachedResources().readText("Scenes/" + this.name + "/manifest.json"));
            JSONArray spawnPositionArray = sceneObject.getJSONArray("spawn");
            this.spawnPosition = new Position(spawnPositionArray.getFloat(0), spawnPositionArray.getFloat(1));
            width = sceneObject.getJSONArray("size").getInt(0);
            height = sceneObject.getJSONArray("size").getInt(1);
            this.layers = new HashMap<>();
            JSONArray jsonLayers = sceneObject.getJSONArray("layers");
            jsonLayers.forEach(_jsonLayer -> {
                JSONObject jsonLayer = (JSONObject) _jsonLayer;
                Layer newLayer;
                String layerName = jsonLayer.getString("name");
                switch (layerName) {
                    case "floor":
                        newLayer = new LowerLayer(name, width, height, 0);
                        break;
                    case "ground":
                        newLayer = new LowerLayer(name, width, height, 1);
                        break;
                    case "bush":
                        newLayer = new BushLayer(name, width, height, 2);
                        break;
                    case "object":
                        newLayer = new ObjectLayer(name, width, height, 3);
                        break;
                    case "sky":
                        newLayer = new UpperLayer(name, width, height, 4);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown layer with name " + layerName);
                }
                int cur = 0;
                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        int v = jsonLayer.getJSONArray("data").getInt(cur++);
                        if (v != 0) {
                            newLayer.setTile(x, y, new Tile(Integer.toString(v)));
                        }
                    }
                }
                layers.put(layerName, newLayer);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tick(TickMessageHandler handler) {
        super.tick(handler);
    }

    @Override
    public void yieldUpdateMessage(TickMessageHandler handler) {
        super.yieldUpdateMessage(handler);
        handler.submit(new SCMessage(SCMessage.MSG_UPDATE_SCENE_DATA, SceneData.generateSceneData(world, this).clone()));
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }

    public Layer getLayer(String name) {
        return layers.get(name);
    }

    public HashMap<String, Layer> getLayers() {
        return layers;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public UUID getUUID() {
        return uuid;
    }
}
