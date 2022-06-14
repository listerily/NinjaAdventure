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
    private final App app;

    public Renderer(App app) {
        this.app = app;
    }

    public void drawGraphics(ClientDataManager clientDataManager, Graphics graphics, Dimension size) {
        ResourceManager resourceManager = app.getResourceManager();
        CachedResources cachedResources = resourceManager.getCachedResources();
        SceneData sceneData = clientDataManager.getCurrentSceneDataClone();
        PlayerData playerData = clientDataManager.getSelfPlayerClone();
        if (sceneData != null) {
            drawScene(cachedResources, graphics, size, sceneData, playerData);
        }
    }

    public void drawScene(CachedResources cachedResources, Graphics graphics, Dimension size, SceneData sceneData, PlayerData playerData) {
        TileData[][] tileData = sceneData.tileSheet;
        int width = size.width;
        int height = size.height;
        int tileWidth = width / sceneData.width;
        int tileHeight = height / sceneData.height;
        for (int x = 0; x < sceneData.width; ++x) {
            for (int y = 0; y < sceneData.height; ++y) {
                if (tileData != null && tileData[x][y] != null) {
                    if (tileData[x][y].tileStackLower != null) {
                        drawTiles(cachedResources, graphics, tileData[x][y].tileStackLower, tileWidth, tileHeight, x, y);
                    }
                }
            }
        }

        if (playerData != null) {
            if (sceneData.playerData != null) {
                for (PlayerData otherPlayerData : sceneData.playerData) {
                    if (otherPlayerData.uuid != playerData.uuid) {
                        drawPlayer(cachedResources, graphics, otherPlayerData, tileWidth, tileHeight);
                    }
                }
            }
            drawPlayer(cachedResources, graphics, playerData, tileWidth, tileHeight);
        }

        for (int x = 0; x < sceneData.width; ++x) {
            for (int y = 0; y < sceneData.height; ++y) {
                if (tileData != null && tileData[x][y] != null) {
                    // Render tiles lower
                    if (tileData[x][y].tileStackUpper != null) {
                        drawTiles(cachedResources, graphics, tileData[x][y].tileStackUpper, tileWidth, tileHeight, x, y);
                    }
                }
            }
        }
        drawHUD(cachedResources, graphics, tileWidth, tileHeight, size);
    }

    public void drawPlayer(CachedResources cachedResources, Graphics graphics, PlayerData playerData, int tileWidth, int tileHeight) {
        Image image;
        try {
            image = cachedResources.readImage("Characters/" + playerData.character + "/SeparateAnim/Idle.png");
            graphics.drawImage(image, (int) (playerData.position.x * tileWidth - tileWidth * 0.5), (int) (playerData.position.y * tileWidth - tileWidth * 0.5),
                    tileWidth, tileHeight, null);
            Font textFont = app.getResourceManager().getCachedResources().readFont(Font.TRUETYPE_FONT, "HUD/Font/NormalFont.ttf").deriveFont(15f);
            graphics.setFont(textFont);
            FontMetrics metrics = graphics.getFontMetrics(textFont);
            graphics.setColor(Color.WHITE);
            graphics.drawString(playerData.nickname,
                    (int) (playerData.position.x * tileWidth - metrics.stringWidth(playerData.nickname) / 2),
                    (int) (playerData.position.y * tileWidth - tileWidth * 0.5 - metrics.getHeight()));
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading character resource. Skipped drawing.", e);
        } catch (FontFormatException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading font resource. Skipped drawing.", e);
        }
    }

    public void drawTiles(CachedResources cachedResources, Graphics graphics, String[] tileStack, int tileWidth, int tileHeight, int x, int y) {
        for (String tileId : tileStack) {
            try {
                Image image = cachedResources.readImage("Tiles/tile_" + tileId + ".png");
                graphics.drawImage(image, x * tileWidth, y * tileHeight, tileWidth, tileHeight, null);
            } catch (IOException e) {
                app.getAppLogger().log(Level.WARNING, "IO Error while reading tile resource. Skipped drawing.", e);
            }
        }
    }
    public void drawHUD(CachedResources cachedResources, Graphics graphics, int tileWidth, int tileHeight, Dimension size) {

    }
}
