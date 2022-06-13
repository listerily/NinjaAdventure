package net.listerily.NinjaAdventure.server.data.layers;

public class LowerLayer extends Layer {
    public LowerLayer(String name, int width, int height, int priority) {
        super(name, width, height, priority);
    }

    @Override
    public boolean isLowerLayer() {
        return true;
    }

    @Override
    public void interact(int x, int y) {
        super.interact(x, y);
    }
}
