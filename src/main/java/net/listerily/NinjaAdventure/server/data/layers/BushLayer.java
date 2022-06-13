package net.listerily.NinjaAdventure.server.data.layers;

public class BushLayer extends LowerLayer {
    public BushLayer(String name, int width, int height, int priority) {
        super(name, width, height, priority);
    }

    @Override
    public boolean ableToInteract(int x, int y) {
        return tileSheet[x][y] != null;
    }

    @Override
    public boolean ableToStep(int x, int y) {
        return tileSheet[x][y] != null;
    }
}
