package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.entities.Entity;
import net.listerily.NinjaAdventure.server.data.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World {
    private final HashMap<Integer, Scene> scenes;
    private ArrayList<Entity> entities;
    public World(ResourceManager resourceManager) {
        scenes = new HashMap<>();
        scenes.put(0, new Scene(resourceManager, 0));
        entities = new ArrayList<>();
    }

    public synchronized void tick(TickMessageHandler handler) {
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
        addEntity(player);
    }

    public synchronized void removePlayer(UUID playerUUID) {
        entities = entities.stream()
                .filter(entity -> !(entity instanceof Player) || !((Player)entity).getPlayerUUID().equals(playerUUID))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
