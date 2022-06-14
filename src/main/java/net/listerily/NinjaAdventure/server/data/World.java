package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.entities.Entity;
import net.listerily.NinjaAdventure.server.data.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World extends TickingObject {
    private final HashMap<Integer, Scene> scenes;
    private final ArrayList<Entity> entities;
    public World(ResourceManager resourceManager) {
        scenes = new HashMap<>();
        scenes.put(0, new Scene(resourceManager, this, 0));
        entities = new ArrayList<>();
    }

    public synchronized void tick(TickMessageHandler handler) {
        super.tick(handler);
        if (scenes != null)
            scenes.forEach((id, scene) -> scene.tick(handler));
        if (entities != null)
            entities.forEach(entity -> entity.tick(handler));
    }

    public synchronized void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public synchronized void newPlayer(UUID playerUUID) {
        Player player = new Player(this, playerUUID);
        player.setScene(scenes.get(0));
        player.moveToSpawn();
        player.markUpdated();
        scenes.get(0).markUpdated();
        addEntity(player);
    }

    public synchronized void removePlayer(UUID playerUUID) {
        Player target = findPlayer(playerUUID);
        if (target != null) {
            target.getScene().markUpdated();
            entities.remove(target);
        }
    }

    public synchronized Player findPlayer(UUID playerUUID) {
        for (Entity entity : entities) {
            if (entity instanceof Player && ((Player) entity).getPlayerUUID().equals(playerUUID)) {
                return (Player) entity;
            }
        }
        return null;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public HashMap<Integer, Scene> getScenes() {
        return scenes;
    }
}
