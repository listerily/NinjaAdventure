package net.listerily.NinjaAdventure.server.data.layers;

public class ObjectLayer extends LowerLayer {

    public ObjectLayer(String name, int width, int height, int priority) {
        super(name, width, height, priority);
    }

    @Override
    public boolean ableToStep(int x, int y) {
        return tileSheet[x][y] != null;
    }
}
