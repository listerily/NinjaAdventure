package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.util.Position;

public class Entity {
    protected final World world;
    protected Scene scene;
    protected Position position;
    protected int health;

    public Entity(World world) {
        this.world = world;
        this.position = new Position();
        this.health = 0;
    }

    public void movePosition(float dx, float dy) {
        position.move(dx, dy);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setPosition(Position position) {
        this.position.set(position);
    }

    public void tick(TickMessageHandler handler) {

    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return 0;
    }

    public void hurt(int point, Entity attacker) {
        health -= point;
        if (health <= 0) {
            die(attacker);
        }
    }

    public void die(Entity attacker) {

    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public Position getPosition() {
        return position;
    }
}
