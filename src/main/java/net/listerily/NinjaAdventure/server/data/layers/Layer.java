package net.listerily.NinjaAdventure.server.data.layers;

import net.listerily.NinjaAdventure.server.data.Tile;

public abstract class Layer {
    protected final Tile[][] tileSheet;
    protected final String name;
    protected final int priority;
    public Layer(String name, int width, int height, int priority) {
        this.name = name;
        this.priority = priority;
        tileSheet = new Tile[width][height];
    }

    public void setTile(int x, int y, Tile tile) {
        tileSheet[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        return tileSheet[x][y];
    }

    public String getName() {
        return name;
    }

    public boolean isUpperLayer() {
        return false;
    }

    public boolean isLowerLayer() {
        return false;
    }

    public boolean ableToStep(int x, int y) {
        return true;
    }

    public boolean ableToInteract(int x, int y) {
        return false;
    }

    public void interact(int x, int y) {

    }

    public int getDisplayPriority() {
        return priority;
    }
}
