package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.communication.MonsterData;
import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.communication.SceneData;
import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.entities.Monster;
import net.listerily.NinjaAdventure.server.data.layers.*;
import net.listerily.NinjaAdventure.util.Position;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Scene extends TickingObject {
    protected String name;
    protected Position spawnPosition;
    protected HashMap<String, Layer> layers;
    protected HashMap<PortalPosition, Integer> portalPositions;
    protected HashMap<Integer, Portal> portals;
    protected int width, height, variant;
    protected ArrayList<String> weathers;
    protected ArrayList<Monster> monsters;
    protected final UUID uuid;
    protected final World world;

    public Scene(ResourceManager resourceManager, World world, UUID uuid, int type, Scene prevScene) {
        this.uuid = uuid;
        this.world = world;
        try {
            JSONArray scenes = new JSONArray(resourceManager.getCachedResources().readText("Scenes/scene.json"));
            int index = 0;
            if (type == 1) {
                do {
                    index = 1 + new Random().nextInt(scenes.length() - 1);
                } while (prevScene != null && index == prevScene.variant);
            }
            JSONObject sceneInfo = scenes.getJSONObject(variant = index);
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
                        newLayer = new LowerLayer(layerName, width, height, 0);
                        break;
                    case "ground":
                        newLayer = new LowerLayer(layerName, width, height, 1);
                        break;
                    case "bush":
                        newLayer = new BushLayer(layerName, width, height, 2);
                        break;
                    case "object":
                        newLayer = new ObjectLayer(layerName, width, height, 3);
                        break;
                    case "sky":
                        newLayer = new UpperLayer(layerName, width, height, 4);
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
            this.weathers = new ArrayList<>();
            JSONArray jsonWeathers = sceneObject.getJSONArray("weathers");
            jsonWeathers.forEach(_weather -> {
                String weather = (String) _weather;
                this.weathers.add(weather);
            });
            this.portals = new HashMap<>();
            this.portalPositions = new HashMap<>();
            JSONArray jsonPortals = sceneObject.getJSONArray("portals");
            jsonPortals.forEach(_jsonPortal -> {
                JSONObject jsonPortal = (JSONObject) _jsonPortal;
                int x = jsonPortal.getJSONArray("position").getInt(0);
                int y = jsonPortal.getJSONArray("position").getInt(1);
                PortalPosition portalPosition = new PortalPosition();
                portalPosition.x = x;
                portalPosition.y = y;
                int portalId = jsonPortal.getInt("id");
                int portalType = jsonPortal.getInt("type");
                portalPositions.put(portalPosition, portalId);
                Position returnPosition = new Position();
                returnPosition.x = jsonPortal.getJSONArray("return").getFloat(0);
                returnPosition.y = jsonPortal.getJSONArray("return").getFloat(1);
                if (portalType == 1) {
                    Portal portal = new Portal();
                    portal.type = portalType;
                    portal.scene = null;
                    portal.returnPosition = returnPosition;
                    portals.put(portalId, portal);
                } else if (portalType == -1) {
                    Portal portal = new Portal();
                    portal.type = portalType;
                    portal.scene = prevScene;
                    portal.returnPosition = returnPosition;
                    portals.put(portalId, portal);
                }
            });
            this.monsters = new ArrayList<>();
            JSONArray jsonMonsters = sceneObject.getJSONArray("monsters");
            jsonMonsters.forEach(_jsonMonster -> {
                JSONArray monsterPosition = (JSONArray) _jsonMonster;
                float monsterPositionX = monsterPosition.getFloat(0);
                float monsterPositionY = monsterPosition.getFloat(1);
                Monster newMonster = new Monster(world, UUID.randomUUID());
                newMonster.setScene(this);
                newMonster.setPosition(monsterPositionX, monsterPositionY);
                this.monsters.add(newMonster);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tick(TickMessageHandler handler) {
        super.tick(handler);
        this.monsters.forEach(monster -> monster.tick(handler));
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public ArrayList<String> getWeathers() {
        return weathers;
    }

    public static class PortalPosition implements Serializable {
        public int x, y;
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PortalPosition) {
                PortalPosition rhs = (PortalPosition) obj;
                return x == rhs.x && y == rhs.y;
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return x << 16 | y;
        }
    }

    public static class Portal {
        public Scene scene;
        public int type;
        public Position returnPosition;
    }
}
