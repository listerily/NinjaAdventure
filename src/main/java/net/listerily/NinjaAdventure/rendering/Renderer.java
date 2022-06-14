package net.listerily.NinjaAdventure.rendering;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;
import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SceneData;
import net.listerily.NinjaAdventure.communication.TileData;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;

public class Renderer {
    private App app;

    public Renderer(App app) {
        this.app = app;
    }

    public void drawGraphics(ClientDataManager clientDataManager, Graphics graphics, Dimension size) {
        ResourceManager resourceManager = app.getResourceManager();
        CachedResources cachedResources = resourceManager.getCachedResources();
        SceneData sceneData = clientDataManager.getCurrentSceneDataClone();
        PlayerData playerData = clientDataManager.getSelfPlayerClone();
        if (sceneData != null) {
            TileData[][] tileData = sceneData.tileSheet;
            int width = size.width;
            int height = size.height;
            int imageWidth = width / sceneData.width;
            int imageHeight = height / sceneData.height;
            for (int x = 0; x < sceneData.width; ++x) {
                for (int y = 0; y < sceneData.height; ++y) {
                    if (tileData != null && tileData[x][y] != null) {
                        if (tileData[x][y].tileStackLower != null) {
                            for (String tileId : tileData[x][y].tileStackLower) {
                                try {
                                    Image image = cachedResources.readImage("Tiles/tile_" + tileId + ".png");
                                    graphics.drawImage(image, x * imageWidth, y * imageHeight, imageWidth, imageHeight, null);
                                } catch (IOException e) {
                                    app.getAppLogger().log(Level.WARNING, "IO Error while reading tile resource. Skipped drawing.", e);
                                }
                            }
                        }
                        if (tileData[x][y].tileStackUpper != null) {
                            for (String tileId : tileData[x][y].tileStackUpper) {
                                try {
                                    Image image = cachedResources.readImage("Tiles/tile_" + tileId + ".png");
                                    graphics.drawImage(image, x * imageWidth, y * imageHeight, imageWidth, imageHeight, null);
                                } catch (IOException e) {
                                    app.getAppLogger().log(Level.WARNING, "IO Error while reading tile resource. Skipped drawing.", e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
