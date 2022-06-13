package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.layers.*;
import net.listerily.NinjaAdventure.util.Position;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Scene {
    protected String name;
    protected Position spawnPosition;
    protected HashMap<String, Layer> layers;
    public Scene(ResourceManager resourceManager, int id) {
        try {
            JSONArray scenes = new JSONArray(resourceManager.getCachedResources().readText("Scenes/scene.json"));
            JSONObject sceneInfo = scenes.getJSONObject(id);
            this.name = sceneInfo.getString("name");
            JSONObject sceneObject = new JSONObject(resourceManager.getCachedResources().readText("Scenes/" + this.name + "/manifest.json"));
            JSONArray spawnPositionArray = sceneObject.getJSONArray("spawn");
            this.spawnPosition = new Position(spawnPositionArray.getFloat(0), spawnPositionArray.getFloat(1));
            int sceneWidth = sceneObject.getJSONArray("size").getInt(0);
            int sceneHeight = sceneObject.getJSONArray("size").getInt(1);
            this.layers = new HashMap<>();
            JSONArray jsonLayers = sceneObject.getJSONArray("layers");
            jsonLayers.forEach(_jsonLayer -> {
                JSONObject jsonLayer = (JSONObject) _jsonLayer;
                Layer newLayer;
                String layerName = jsonLayer.getString("name");
                switch (layerName) {
                    case "ground":
                        newLayer = new LowerLayer(name, sceneWidth, sceneHeight, 0);
                        break;
                    case "floor":
                        newLayer = new LowerLayer(name, sceneWidth, sceneHeight, 1);
                        break;
                    case "bush":
                        newLayer = new BushLayer(name, sceneWidth, sceneHeight, 2);
                        break;
                    case "object":
                        newLayer = new ObjectLayer(name, sceneWidth, sceneHeight, 3);
                        break;
                    case "sky":
                        newLayer = new UpperLayer(name, sceneWidth, sceneHeight, 4);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown layer with name " + layerName);
                }
                layers.put(layerName, newLayer);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void tick(TickMessageHandler handler) {

    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }

    public Layer getLayer(String name) {
        return layers.get(name);
    }
}
