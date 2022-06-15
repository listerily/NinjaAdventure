package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.Scene;
import net.listerily.NinjaAdventure.server.data.TickingObject;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.server.data.layers.Layer;
import net.listerily.NinjaAdventure.util.Position;

import java.util.ArrayList;
import java.util.UUID;

public class Entity extends TickingObject {
    protected UUID uuid;
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

    public Entity(World world, UUID uuid) {
        this.uuid = uuid;
        this.world = world;
        this.position = new Position();
        this.health = getMaxHealth();
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
        this.hurting = 4;
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
        this.actioning = 5;
    }

    public void turn(float x, float y) {
        if (y > 0) {
            facing = FACING_DOWN;
        } else if (y < 0) {
            facing = FACING_UP;
        } else if (x > 0) {
            facing = FACING_RIGHT;
        } else if (x < 0) {
            facing = FACING_LEFT;
        }
    }

    public void walk(float x, float y) {
        if (isDead() || isAttacking())
            return;
        turn(x, y);
        if (isWalkable(getPosition().x + x, getPosition().y + y)) {
            setWalkingAction();
            movePosition(x, y);
        } else {
            float length = (float) Math.sqrt(x * x + y * y) * 0.6f;
            for (Position facingPosition : getFacingPositions()) {
                if (isTileWalkable((int) facingPosition.x, (int) facingPosition.y)) {
                    float targetX = facingPosition.x + 0.5f;
                    float targetY = facingPosition.y + 0.5f;
                    if (targetX > position.x && isWalkable(position.x + 0.05f, position.y))
                        movePosition(length, 0.0f);
                    if (targetX < position.x && isWalkable(position.x - 0.05f, position.y))
                        movePosition(-length, 0.0f);
                    if (targetY > position.y && isWalkable(position.x, position.y + length))
                        movePosition(0.0f, length);
                    if (targetY < position.y && isWalkable(position.x, position.y - length))
                        movePosition(0.0f, -length);
                    break;
                }
            }
        }
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

    public UUID getUUID() {
        return uuid;
    }

    private boolean isTileWalkable(int x, int y) {
        if (x < 0 || x >= scene.getWidth() || y < 0 || y >= scene.getHeight())
            return false;
        for (Layer layer : scene.getLayers().values()) {
            if (!layer.ableToStep(x, y))
                return false;
        }
        return true;
    }

    private boolean isWalkable(float x, float y) {
        float collisionBoxRadius = getCollisionBoxRadius();
        return isTileWalkable((int) Math.floor(x - collisionBoxRadius), (int) Math.floor(y - collisionBoxRadius)) &&
                isTileWalkable((int) Math.floor(x + collisionBoxRadius), (int) Math.floor(y + collisionBoxRadius)) &&
                isTileWalkable((int) Math.floor(x - collisionBoxRadius), (int) Math.floor(y + collisionBoxRadius)) &&
                isTileWalkable((int) Math.floor(x + collisionBoxRadius), (int) Math.floor(y - collisionBoxRadius));
    }

    private float getCollisionBoxRadius() {
        return 0.4f;
    }

    private ArrayList<Position> getFacingPositions() {
        float collisionBoxRadius = getCollisionBoxRadius();
        ArrayList<Position> result = new ArrayList<>();
        switch (getFacing())
        {
            case FACING_UP:
                result.add(new Position((float) Math.floor(getPosition().x - collisionBoxRadius), (float) (Math.floor(getPosition().y) - 1)));
                result.add(new Position((float) Math.floor(getPosition().x + collisionBoxRadius), (float) (Math.floor(getPosition().y) - 1)));
                return result;
            case FACING_DOWN:
                result.add(new Position((float) Math.floor(getPosition().x - collisionBoxRadius), (float) (Math.floor(getPosition().y) + 1)));
                result.add(new Position((float) Math.floor(getPosition().x + collisionBoxRadius), (float) (Math.floor(getPosition().y) + 1)));
                return result;
            case FACING_LEFT:
                result.add(new Position((float) (Math.floor(getPosition().x) - 1), (float) Math.floor(getPosition().y - collisionBoxRadius)));
                result.add(new Position((float) (Math.floor(getPosition().x) - 1), (float) Math.floor(getPosition().y + collisionBoxRadius)));
                return result;
            case FACING_RIGHT:
                result.add(new Position((float) (Math.floor(getPosition().x) + 1), (float) Math.floor(getPosition().y - collisionBoxRadius)));
                result.add(new Position((float) (Math.floor(getPosition().x) + 1), (float) Math.floor(getPosition().y + collisionBoxRadius)));
                return result;
        }
        return result;
    }

    private Position getFacingPosition() {
        float x = getPosition().x;
        float y = getPosition().y;
        switch (getFacing()) {
            case FACING_UP:
                return new Position((float) Math.floor(x), (float) (Math.floor(y) - 1));
            case FACING_DOWN:
                return new Position((float) Math.floor(x), (float) (Math.floor(y) + 1));
            case FACING_LEFT:
                return new Position((float) (Math.floor(x) - 1), (float) Math.floor(y));
            case FACING_RIGHT:
                return new Position((float) (Math.floor(x) + 1), (float) Math.floor(y));
        }
        return new Position();
    }
}
