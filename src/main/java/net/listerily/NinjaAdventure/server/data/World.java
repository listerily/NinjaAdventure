package net.listerily.NinjaAdventure.server.data;

import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.entities.Entity;
import net.listerily.NinjaAdventure.server.data.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World extends TickingObject {
    private final HashMap<UUID, Scene> scenes;
    private final ArrayList<Entity> entities;
    private final ResourceManager resourceManager;
    private final UUID spawnSceneUUID;
    public World(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        scenes = new HashMap<>();
        spawnSceneUUID = UUID.randomUUID();
        scenes.put(spawnSceneUUID, new Scene(resourceManager, this, spawnSceneUUID, 0, null));
        entities = new ArrayList<>();
    }

    public synchronized void tick(TickMessageHandler handler) {
        super.tick(handler);
        if (scenes != null)
            scenes.forEach((id, scene) -> scene.tick(handler));
        if (entities != null)
            entities.forEach(entity -> entity.tick(handler));
        teleportPlayers(handler);
    }

    private synchronized void teleportPlayers(TickMessageHandler handler) {
        if (scenes != null && entities != null) {
            for (Entity entity : getEntities()) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    Scene.PortalPosition portalPosition = new Scene.PortalPosition();
                    portalPosition.x = (int) player.getPosition().x;
                    portalPosition.y = (int) player.getPosition().y;
                    Scene playerCurrentScene = player.getScene();
                    if (playerCurrentScene.portalPositions.containsKey(portalPosition)) {
                        int portalId = playerCurrentScene.portalPositions.get(portalPosition);
                        UUID newSceneUUID;
                        if (playerCurrentScene.portals.containsKey(portalId) && playerCurrentScene.portals.get(portalId).scene != null) {
                            Scene.Portal portal = playerCurrentScene.portals.get(portalId);
                            newSceneUUID = portal.scene.getUUID();
                            player.setScene(portal.scene);
                            playerCurrentScene.markUpdated();
                            portal.scene.markUpdated();
                            player.markUpdated();
                            if (portal.type == 1) {
                                player.moveToSpawn();
                            } else {
                                // Move back
                                for (Scene.Portal portalPrev : portal.scene.portals.values()) {
                                    if (portalPrev.scene == playerCurrentScene) {
                                        player.setPosition(portalPrev.returnPosition);
                                    }
                                }
                            }
                        } else {
                            newSceneUUID = UUID.randomUUID();
                            Scene newScene = new Scene(resourceManager, this, newSceneUUID, 1, playerCurrentScene);
                            scenes.put(newSceneUUID, newScene);
                            Scene.Portal portal = playerCurrentScene.portals.get(portalId);
                            portal.scene = newScene;
                            player.setScene(newScene);
                            player.moveToSpawn();
                            playerCurrentScene.markUpdated();
                            newScene.markUpdated();
                            player.markUpdated();
                        }
                        handler.submitTo(new SCMessage(SCMessage.MSG_SWITCH_SCENE, newSceneUUID), player.getPlayerUUID());
                    }
                }
            }
        }
    }

    public synchronized void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public synchronized void newPlayer(UUID playerUUID) {
        Player player = new Player(this, playerUUID);
        player.setScene(scenes.get(spawnSceneUUID));
        player.moveToSpawn();
        player.markUpdated();
        scenes.get(spawnSceneUUID).markUpdated();
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

    public HashMap<UUID, Scene> getScenes() {
        return scenes;
    }
}
