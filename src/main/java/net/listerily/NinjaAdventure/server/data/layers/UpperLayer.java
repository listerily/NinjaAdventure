package net.listerily.NinjaAdventure.server.data.layers;

import net.listerily.NinjaAdventure.server.data.layers.Layer;

public class UpperLayer extends Layer {
    public UpperLayer(String name, int width, int height, int priority) {
        super(name, width, height, priority);
    }

    @Override
    public boolean isUpperLayer() {
        return true;
    }
}
