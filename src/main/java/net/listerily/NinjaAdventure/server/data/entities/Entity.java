package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.TickingObject;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.util.Position;

public class Entity extends TickingObject {
    protected final World world;
    protected Scene scene;
    protected Position position;
    protected int health;
    protected int actionState;
    protected boolean dead;
    protected int hurting;
    protected int actioning;
    protected int facing;
    public static final int ACTION_IDLE = 0;
    public static final int ACTION_WALKING = 1;
    public static final int ACTION_ATTACK = 2;
    public static final int FACING_DOWN = 0;
    public static final int FACING_LEFT = 1;
    public static final int FACING_UP = 2;
    public static final int FACING_RIGHT = 3;

    public Entity(World world) {
        this.world = world;
        this.position = new Position();
        this.health = 0;
        this.actionState = ACTION_IDLE;
        this.dead = false;
        this.hurting = 0;
        this.actioning = 0;
        this.facing = FACING_DOWN;
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

    @Override
    public void tick(TickMessageHandler handler) {
        super.tick(handler);
        if (this.hurting > 0) {
            --this.hurting;
        } else if (this.hurting == 0) {
            markUpdated();
        }
        if (this.actioning > 0)
            --this.actioning;
        else if (this.actioning == 0) {
            this.actionState = ACTION_IDLE;
            markUpdated();
        }
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
        this.hurting = 1;
        if (health <= 0) {
            die(attacker);
        }
    }

    public void setWalkingAction() {
        this.actionState = ACTION_WALKING;
        this.actioning = 1;
    }

    public void setAttackingAction() {
        this.actionState = ACTION_ATTACK;
        this.actioning = 1;
    }

    public void walk(float x, float y) {
        setWalkingAction();
        if (y > 0) {
            facing = FACING_DOWN;
        } else if (y < 0) {
            facing = FACING_UP;
        } else if (x > 0) {
            facing = FACING_RIGHT;
        } else if (x < 0) {
            facing = FACING_LEFT;
        }
        movePosition(x, y);
    }

    public void die(Entity attacker) {
        dead = true;
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

    public boolean isDead() {
        return dead;
    }

    public boolean isHurting() {
        return this.hurting > 0;
    }

    public boolean isAttacking() {
        return getActionState() == ACTION_ATTACK;
    }

    public boolean isWalking() {
        return getActionState() == ACTION_WALKING;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getActionState() {
        return actionState;
    }

}
