package net.listerily.NinjaAdventure.rendering;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.client.ClientDataManager;
import net.listerily.NinjaAdventure.communication.MonsterData;
import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SceneData;
import net.listerily.NinjaAdventure.communication.TileData;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.server.data.entities.Entity;
import net.listerily.NinjaAdventure.server.data.entities.Player;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class Renderer {
    private final App app;
    private int animationIndicator;

    public Renderer(App app) {
        this.app = app;
        this.animationIndicator = 1114514;
    }

    public void drawGraphics(ClientDataManager clientDataManager, Graphics graphics, Dimension size) {
        animationIndicator = (animationIndicator + 1) % 0x7fffffff;
        ResourceManager resourceManager = app.getResourceManager();
        CachedResources cachedResources = resourceManager.getCachedResources();
        SceneData sceneData = clientDataManager.getCurrentSceneDataClone();
        PlayerData playerData = clientDataManager.getSelfPlayerClone();
        if (sceneData != null) {
            drawScene(cachedResources, graphics, size, sceneData, playerData);
            int tileWidth = size.width / sceneData.width;
            int tileHeight = size.height / sceneData.height;
            drawWeather(cachedResources, graphics, size, sceneData, tileWidth, tileHeight);
            if (playerData != null) {
                drawHUD(cachedResources, graphics, size, tileWidth, tileHeight, playerData);
            }
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

        if (sceneData.monsterData != null) {
            for (MonsterData monsterData : sceneData.monsterData) {
                drawMonster(cachedResources, graphics, monsterData, tileWidth, tileHeight);
            }
        }

        if (playerData != null) {
            if (sceneData.playerData != null) {
                for (PlayerData otherPlayerData : sceneData.playerData) {
                    if (otherPlayerData.uuid != playerData.uuid) {
                        drawPlayer(cachedResources, graphics, otherPlayerData, tileWidth, tileHeight, true);
                    }
                }
            }
            drawPlayer(cachedResources, graphics, playerData, tileWidth, tileHeight, false);
        }

        for (int x = 0; x < sceneData.width; ++x) {
            for (int y = 0; y < sceneData.height; ++y) {
                if (tileData != null && tileData[x][y] != null) {
                    if (tileData[x][y].tileStackUpper != null) {
                        drawTiles(cachedResources, graphics, tileData[x][y].tileStackUpper, tileWidth, tileHeight, x, y);
                    }
                }
            }
        }
    }

    public void drawPlayer(CachedResources cachedResources, Graphics graphics, PlayerData playerData, int tileWidth, int tileHeight, boolean other) {
        try {
            if (playerData.dead) {
                BufferedImage deadImage = cachedResources.readImage("Characters/" + playerData.character + "/SeparateAnim/Dead.png");
                drawPlayerImage(cachedResources, deadImage, graphics, playerData, tileWidth, tileHeight, other);
            } else if (playerData.actionState == Player.ACTION_IDLE) {
                BufferedImage idleImage = cachedResources.readImage("Characters/" + playerData.character + "/SeparateAnim/Idle.png");
                BufferedImage targetImage;
                if (playerData.facing == Player.FACING_DOWN) {
                    targetImage = idleImage.getSubimage(0, 0, 16, 16);
                } else if (playerData.facing == Player.FACING_UP) {
                    targetImage = idleImage.getSubimage(16, 0, 16, 16);
                } else if (playerData.facing == Player.FACING_LEFT) {
                    targetImage = idleImage.getSubimage(32, 0, 16, 16);
                } else if (playerData.facing == Player.FACING_RIGHT) {
                    targetImage = idleImage.getSubimage(48, 0, 16, 16);
                } else {
                    app.getAppLogger().log(Level.WARNING, "Illegal facing " + playerData.facing + ", skipped drawing.");
                    return;
                }
                drawPlayerImage(cachedResources, targetImage, graphics, playerData, tileWidth, tileHeight, other);
            } else if (playerData.actionState == Player.ACTION_WALKING) {
                BufferedImage walkingImage = cachedResources.readImage("Characters/" + playerData.character + "/SeparateAnim/Walk.png");
                BufferedImage targetImage;
                if (playerData.facing == Player.FACING_DOWN) {
                    targetImage = walkingImage.getSubimage(0, 16 * ((animationIndicator / 8) % 4), 16, 16);
                } else if (playerData.facing == Player.FACING_UP) {
                    targetImage = walkingImage.getSubimage(16, 16 * ((animationIndicator / 8) % 4), 16, 16);
                } else if (playerData.facing == Player.FACING_LEFT) {
                    targetImage = walkingImage.getSubimage(32, 16 * ((animationIndicator / 8) % 4), 16, 16);
                } else if (playerData.facing == Player.FACING_RIGHT) {
                    targetImage = walkingImage.getSubimage(48, 16 * ((animationIndicator / 8) % 4), 16, 16);
                } else {
                    app.getAppLogger().log(Level.WARNING, "Illegal facing " + playerData.facing + ", skipped drawing.");
                    return;
                }
                drawPlayerImage(cachedResources, targetImage, graphics, playerData, tileWidth, tileHeight, other);
            } else if (playerData.actionState == Player.ACTION_ATTACK) {
                BufferedImage attackImage = cachedResources.readImage("Characters/" + playerData.character + "/SeparateAnim/Attack.png");
                BufferedImage targetImage;
                int extra = animationIndicator % 5;
                if (playerData.facing == Player.FACING_DOWN) {
                    targetImage = attackImage.getSubimage(0, 0, 16, 16);
                    graphics.drawImage(cachedResources.readImage("Weapon/WeaponDown.png"),
                            (int) (playerData.position.x * tileWidth - tileWidth * 7 * 0.05 / 2 - tileWidth * 0.2),
                            (int) (playerData.position.y * tileHeight - tileWidth * 21 * 0.05 / 2 + tileHeight * 0.6) + extra,
                            (int) (tileWidth * 7 * 0.05), (int) (tileHeight * 21 * 0.05), null);
                } else if (playerData.facing == Player.FACING_UP) {
                    graphics.drawImage(cachedResources.readImage("Weapon/WeaponUp.png"),
                            (int) (playerData.position.x * tileWidth - tileWidth * 7 * 0.05 / 2 - tileWidth * 0.2),
                            (int) (playerData.position.y * tileHeight - tileWidth * 21 * 0.05 / 2 - tileHeight * 0.6) - extra,
                            (int) (tileWidth * 7 * 0.05), (int) (tileHeight * 21 * 0.05), null);
                    targetImage = attackImage.getSubimage(16, 0, 16, 16);
                } else if (playerData.facing == Player.FACING_LEFT) {
                    graphics.drawImage(cachedResources.readImage("Weapon/WeaponLeft.png"),
                            (int) (playerData.position.x * tileWidth - tileWidth * 21 * 0.05 / 2 - tileWidth * 0.6) - extra,
                            (int) (playerData.position.y * tileHeight - tileWidth * 7 * 0.05 / 2 + tileHeight * 0.2),
                            (int) (tileWidth * 21 * 0.05), (int) (tileHeight * 7 * 0.05), null);
                    targetImage = attackImage.getSubimage(32, 0, 16, 16);
                } else if (playerData.facing == Player.FACING_RIGHT) {
                    graphics.drawImage(cachedResources.readImage("Weapon/WeaponRight.png"),
                            (int) (playerData.position.x * tileWidth - tileWidth * 21 * 0.05 / 2 + tileWidth * 0.6) + extra,
                            (int) (playerData.position.y * tileHeight - tileWidth * 7 * 0.05 / 2 + tileHeight * 0.2),
                            (int) (tileWidth * 21 * 0.05), (int) (tileHeight * 7 * 0.05), null);
                    targetImage = attackImage.getSubimage(48, 0, 16, 16);
                } else {
                    app.getAppLogger().log(Level.WARNING, "Illegal facing " + playerData.facing + ", skipped drawing.");
                    return;
                }
                drawPlayerImage(cachedResources, targetImage, graphics, playerData, tileWidth, tileHeight, other);
            }
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading character resource. Skipped drawing.", e);
        } catch (FontFormatException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading font resource. Skipped drawing.", e);
        }
    }

    public void drawMonster(CachedResources cachedResources, Graphics graphics, MonsterData monsterData, int tileWidth, int tileHeight) {
        if (monsterData.dead)
            return;
        try {
            String[] monsterNames = new String[]{"Mouse", "Slime", "Snake", "Reptile", "Bamboo", "Dragon", "Lizard", "Mushroom", "Slime2", "Snake2"};
            BufferedImage image = cachedResources.readImage("Actor/Monsters/" + monsterNames[monsterData.type] + ".png");
            BufferedImage targetImage;
            int imageY = 16 * ((animationIndicator / 8) % 4);
            if (monsterData.facing == Entity.FACING_DOWN) {
                targetImage = image.getSubimage(0, imageY, 16, 16);
            } else if (monsterData.facing == Entity.FACING_UP) {
                targetImage = image.getSubimage(16, imageY, 16, 16);
            } else if (monsterData.facing == Entity.FACING_LEFT) {
                targetImage = image.getSubimage(32, imageY, 16, 16);
            } else if (monsterData.facing == Entity.FACING_RIGHT) {
                targetImage = image.getSubimage(48, imageY, 16, 16);
            } else {
                app.getAppLogger().log(Level.WARNING, "Illegal facing " + monsterData.facing + ", skipped drawing.");
                return;
            }
            drawMonsterImage(cachedResources, targetImage, graphics, monsterData, tileWidth, tileHeight);
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading monster resource. Skipped drawing.", e);
        }
    }

    public void drawPlayerImage(CachedResources cachedResources, BufferedImage image, Graphics graphics, PlayerData playerData, int tileWidth, int tileHeight, boolean other) throws IOException, FontFormatException {
        drawEntityImage(image, graphics, playerData.hurting, tileWidth, tileHeight, playerData.position.x, playerData.position.y);
        Font textFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/NormalFont.ttf").deriveFont(18f);
        graphics.setFont(textFont);
        FontMetrics metrics = graphics.getFontMetrics(textFont);
        graphics.setColor(Color.WHITE);
        graphics.drawString(playerData.nickname,
                (int) (playerData.position.x * tileWidth - metrics.stringWidth(playerData.nickname) / 2),
                (int) (playerData.position.y * tileHeight - tileHeight * 0.55));
        if (other) {
            drawLifeBar(cachedResources, graphics, tileWidth, tileHeight, playerData.health, playerData.maxHealth, playerData.position.x, playerData.position.y);
        }
    }

    public void drawMonsterImage(CachedResources cachedResources, BufferedImage image, Graphics graphics, MonsterData monsterData, int tileWidth, int tileHeight) throws IOException {
        drawEntityImage(image, graphics, monsterData.hurting, tileWidth, tileHeight, monsterData.position.x, monsterData.position.y);
        drawLifeBar(cachedResources, graphics, tileWidth, tileHeight, monsterData.health, monsterData.maxHealth, monsterData.position.x, monsterData.position.y);
    }

    public void drawEntityImage(BufferedImage image, Graphics graphics, boolean hurting, int tileWidth, int tileHeight, float x, float y) {
        if (hurting) {
            graphics.drawImage(addTransparentColorOverlay(image, Color.RED, 0, 0.5),
                    (int) (x * tileWidth - tileWidth * 0.45),
                    (int) (y * tileHeight - tileHeight * 0.5),
                    (int) (tileWidth * 0.9), (int) (tileHeight * 0.9), null);
        } else {
            graphics.drawImage(image,
                    (int) (x * tileWidth - tileWidth * 0.45),
                    (int) (y * tileHeight - tileHeight * 0.5),
                    (int) (tileWidth * 0.9), (int) (tileHeight * 0.9), null);
        }
    }

    public void drawLifeBar(CachedResources cachedResources, Graphics graphics, int tileWidth, int tileHeight, int health, int maxHealth, float x, float y) throws IOException {
        BufferedImage lifeBarUnder = cachedResources.readImage("HUD/LifeBarMiniUnder.png");
        BufferedImage lifeBarUpper = cachedResources.readImage("HUD/LifeBarMiniProgress.png");
        int progress = (int) (18.0 * health / maxHealth);
        graphics.drawImage(lifeBarUnder,
                (int) (x * tileWidth - tileWidth * 0.5),
                (int) (y * tileHeight - tileHeight * 0.5),
                tileWidth, tileHeight / 6, null);
        if (progress > 0) {
            graphics.drawImage(lifeBarUpper.getSubimage(0, 0, progress, 4),
                    (int) (x * tileWidth - tileWidth * 0.5),
                    (int) (y * tileHeight - tileHeight * 0.5),
                    (int) (tileWidth * (progress / 18.0)), tileHeight / 6, null);
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

    public void drawWeather(CachedResources cachedResources, Graphics graphics, Dimension size, SceneData sceneData, int tileWidth, int tileHeight) {
        if (sceneData.weathers != null) {
            for (String weather : sceneData.weathers) {
                try {
                    if (weather.equals("cloudy")) {
                        BufferedImage cloudImage = cachedResources.readImage("FX/Particle/Clouds.png");
                        int cloudWidth = (int) (20.0 * tileWidth / 4.0);
                        int cloudHeight = (int) (9.0 * tileHeight / 4.0);
                        int[] primes = {641, 941, 1433, 809, 1163};
                        for (int prime : primes) {
                            Random random = new Random(animationIndicator / prime);
                            if (random.nextBoolean()) {
                                random = new Random(prime + animationIndicator / prime);
                                int startX = random.nextInt((int) (size.width * 0.6));
                                int startY = random.nextInt((int) (size.height * 0.6));
                                int endX = startX + (int) (size.width * 0.5);
                                int endY = startY + (int) (size.height * 0.5);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int x = startX + (int) ((endX - startX) * progress);
                                int y = startY + (int) ((endY - startY) * progress);
                                int alpha = 200;
                                if (progress < 0.1) {
                                    alpha = (int) (255 - progress * 10 * (255 - 200));
                                } else if (progress > 0.9) {
                                    alpha = (int) (255 - (1 - progress) * 10 * (255 - 200));
                                }
                                graphics.drawImage(addTransparentColorOverlay(cloudImage, Color.BLACK, alpha, 0.8),
                                        x, y, cloudWidth, cloudHeight, null);
                            }
                        }
                    }
                    if (weather.equals("rainy")) {
                        BufferedImage rainImageSource1 = cachedResources.readImage("FX/Particle/Rain.png");
                        BufferedImage rainImageSource2 = cachedResources.readImage("FX/Particle/RainOnFloor.png");
                        BufferedImage[] rainImage1 = new BufferedImage[3];
                        for (int i = 0; i < 3; ++i) {
                            rainImage1[i] = rainImageSource1.getSubimage(i * 8, 0, 8, 8);
                        }
                        BufferedImage[] rainImage2 = new BufferedImage[3];
                        for (int i = 0; i < 3; ++i) {
                            rainImage2[i] = rainImageSource2.getSubimage(i * 8, 0, 8, 8);
                        }
                        int rainWidth = (int) (tileWidth / 3.0);
                        int rainHeight = (int) (tileHeight / 3.0);
                        int[] primes = {131, 137, 139, 149, 151, 157, 163};
                        for (int prime : primes) {
                            for (int i = 0; i < 5; ++i) {
                                Random random = new Random(prime ^ (animationIndicator / prime) ^ i);
                                int x = random.nextInt((int) (size.width * 1.5));
                                int y = random.nextInt((int) (size.height * 0.8)) - (int) (size.height * 0.2);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int alpha = 32;
                                if (progress < 0.05) {
                                    alpha = (int) (255 - progress * 20 * (255 - 32));
                                } else if (progress > 0.95) {
                                    alpha = (int) (255 - (1 - progress) * 20 * (255 - 32));
                                }
                                graphics.drawImage(addTransparentColorOverlay(rainImage2[random.nextInt(3)], Color.WHITE, alpha, 0.8),
                                        x, y, rainWidth, rainHeight, null);
                            }
                        }
                        for (int prime : primes) {
                            for (int i = 0; i < 20; ++i) {
                                Random random = new Random(prime + animationIndicator / prime + i);
                                int startX = random.nextInt((int) (size.width * 1.5));
                                int startY = random.nextInt((int) (size.height * 0.8)) - (int) (size.height * 0.2);
                                int endX = startX - (int) (size.width * 0.2);
                                int endY = startY + (int) (size.height * 0.6);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int x = startX + (int) ((endX - startX) * progress);
                                int y = startY + (int) ((endY - startY) * progress);
                                int alpha = 32;
                                if (progress < 0.05) {
                                    alpha = (int) (255 - progress * 20 * (255 - 32));
                                } else if (progress > 0.95) {
                                    alpha = (int) (255 - (1 - progress) * 20 * (255 - 32));
                                }
                                graphics.drawImage(addTransparentColorOverlay(rainImage1[random.nextInt(3)], Color.WHITE, alpha, 0.8),
                                        x, y, rainWidth, rainHeight, null);
                            }
                        }
                    }
                    if (weather.equals("snowy")) {
                        BufferedImage snowImageSource = cachedResources.readImage("FX/Particle/Snow.png");
                        BufferedImage[] snowImage = new BufferedImage[7];
                        for (int i = 0; i < 7; ++i) {
                            snowImage[i] = snowImageSource.getSubimage(i * 8, 0, 8, 8);
                        }
                        int rainWidth = (int) (tileWidth / 3.0);
                        int rainHeight = (int) (tileHeight / 3.0);
                        int[] primes = {131, 137, 139, 149, 151, 157, 163};
                        for (int prime : primes) {
                            for (int i = 0; i < 20; ++i) {
                                Random random = new Random(prime + animationIndicator / prime + i);
                                int startX = random.nextInt((int) (size.width * 1.5));
                                int startY = random.nextInt((int) (size.height * 0.8)) - (int) (size.height * 0.2);
                                int endX = startX - (int) (size.width * 0.2);
                                int endY = startY + (int) (size.height * 0.6);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int x = startX + (int) ((endX - startX) * progress);
                                int y = startY + (int) ((endY - startY) * progress);
                                int alpha = 32;
                                if (progress < 0.05) {
                                    alpha = (int) (255 - progress * 20 * (255 - 32));
                                } else if (progress > 0.95) {
                                    alpha = (int) (255 - (1 - progress) * 20 * (255 - 32));
                                }
                                int snowImageIndex = (int) (progress * 8);
                                if (snowImageIndex >= 7)
                                    snowImageIndex = 6;
                                graphics.drawImage(addTransparentColorOverlay(snowImage[snowImageIndex], Color.WHITE, alpha, 0.8),
                                        x, y, rainWidth, rainHeight, null);
                            }
                        }
                    }
                    if (weather.equals("leafy")) {
                        BufferedImage leafImageSource = cachedResources.readImage("FX/Particle/Leaf.png");
                        BufferedImage[] leafImage = new BufferedImage[6];
                        for (int i = 0; i < 6; ++i) {
                            leafImage[i] = leafImageSource.getSubimage(i * 12, 0, 12, 7);
                        }
                        int leafWidth = (int) (12 * tileWidth / 18.0);
                        int leafHeight = (int) (7 * tileHeight / 18.0);
                        int[] primes = {233, 239, 241, 251, 257, 263, 269};
                        for (int prime : primes) {
                            for (int i = 0; i < 3; ++i) {
                                Random random = new Random((prime) ^ (animationIndicator / prime) + i);
                                int startX = random.nextInt((int) (size.width * 1.5));
                                int startY = random.nextInt((int) (size.height * 0.8)) - (int) (size.height * 0.2);
                                int endX = startX + (int) (size.width * 0.2);
                                int endY = startY + (int) (size.height * 0.6);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int x = startX + (int) ((endX - startX) * progress);
                                int y = startY + (int) ((endY - startY) * progress);
                                int alpha = 0;
                                if (progress < 0.05) {
                                    alpha = (int) (255 - progress * 20 * (255));
                                } else if (progress > 0.95) {
                                    alpha = (int) (255 - (1 - progress) * 20 * (255));
                                }
                                int imageIndex = (int) (progress * 7);
                                if (imageIndex >= 6)
                                    imageIndex = 5;
                                graphics.drawImage(addTransparentColorOverlay(leafImage[imageIndex], Color.WHITE, alpha, 0.0),
                                        x, y, leafWidth, leafHeight, null);
                            }
                        }
                    }
                    if (weather.equals("pink_leafy")) {
                        BufferedImage leafImageSource = cachedResources.readImage("FX/Particle/LeafPink.png");
                        BufferedImage[] leafImage = new BufferedImage[6];
                        for (int i = 0; i < 6; ++i) {
                            leafImage[i] = leafImageSource.getSubimage(i * 12, 0, 12, 7);
                        }
                        int leafWidth = (int) (12 * tileWidth / 18.0);
                        int leafHeight = (int) (7 * tileHeight / 18.0);
                        int[] primes = {233, 239, 241, 251, 257, 263, 269};
                        for (int prime : primes) {
                            for (int i = 0; i < 3; ++i) {
                                Random random = new Random((prime) ^ (animationIndicator / prime) + i);
                                int startX = random.nextInt((int) (size.width * 1.5));
                                int startY = random.nextInt((int) (size.height * 0.8)) - (int) (size.height * 0.2);
                                int endX = startX + (int) (size.width * 0.2);
                                int endY = startY + (int) (size.height * 0.6);
                                float progress = (animationIndicator % prime) / (float) prime;
                                int x = startX + (int) ((endX - startX) * progress);
                                int y = startY + (int) ((endY - startY) * progress);
                                int alpha = 0;
                                if (progress < 0.05) {
                                    alpha = (int) (255 - progress * 20 * (255));
                                } else if (progress > 0.95) {
                                    alpha = (int) (255 - (1 - progress) * 20 * (255));
                                }
                                int imageIndex = (int) (progress * 7);
                                if (imageIndex >= 6)
                                    imageIndex = 5;
                                graphics.drawImage(addTransparentColorOverlay(leafImage[imageIndex], Color.WHITE, alpha, 0.0),
                                        x, y, leafWidth, leafHeight, null);
                            }
                        }
                    }
                } catch (IOException e) {
                    app.getAppLogger().log(Level.WARNING, "IO Error while reading particle resource. Skipped drawing.", e);
                }
            }
        }
    }

    public void drawHUD(CachedResources cachedResources, Graphics graphics, Dimension size, int tileWidth, int tileHeight, PlayerData playerData) {
        try {
            BufferedImage heartImage = cachedResources.readImage("HUD/Heart.png");
            int hp = playerData.health;
            for (int i = 0; i < 3; ++i) {
                int state = 0;
                if (hp > 4) {
                    state = 4;
                    hp -= 4;
                } else {
                    state = hp;
                    hp = 0;
                }
                graphics.drawImage(heartImage.getSubimage(16 * (4 - state), 0, 16, 16),
                        i * tileWidth + tileWidth / 4, tileHeight / 4,
                        tileWidth, tileHeight, null);
            }

            Font textFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/Gameplay.ttf");
            graphics.setFont(textFont.deriveFont(20f));
            graphics.setColor(Color.BLACK);
            String stringToDraw = "Score: " + playerData.score;
            graphics.drawString(stringToDraw, tileWidth / 4, tileHeight + tileHeight / 2);

            if (playerData.dead) {
                graphics.setColor(new Color(0, 0, 0, 127));
                graphics.fillRect(0, 0, size.width, size.height);
                graphics.setFont(textFont.deriveFont(100f));
                FontMetrics metrics = graphics.getFontMetrics(textFont.deriveFont(100f));
                graphics.setColor(Color.RED);
                stringToDraw = "!!YOU DIED!!";
                int heightTitle = metrics.getHeight();
                graphics.drawString(stringToDraw,
                        size.width / 2 - metrics.stringWidth(stringToDraw) / 2,
                        size.height / 2 - heightTitle / 2);
                graphics.setFont(textFont.deriveFont(40f));
                metrics = graphics.getFontMetrics(textFont.deriveFont(40f));
                stringToDraw = "Score: " + playerData.score;
                graphics.drawString(stringToDraw,
                        size.width / 2 - metrics.stringWidth(stringToDraw) / 2,
                        size.height / 2 - metrics.getHeight() / 2 + heightTitle);
            }
        } catch (IOException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading resource. Skipped drawing HUD.", e);
        } catch (FontFormatException e) {
            app.getAppLogger().log(Level.WARNING, "IO Error while reading font. Skipped drawing HUD.", e);
        }
    }

    public static Image addTransparentColorOverlay(BufferedImage im, final Color color, int transparency, double rate) {
        ImageFilter filter = new RGBImageFilter() {
            public final int markerRGB = color.getRGB() | 0xFF000000;

            public int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xff000000) != rgb) {
                    return rgb;
                } else {
                    int r = (0x00ff0000 & rgb) >> 16;
                    int g = (0x0000ff00 & rgb) >> 8;
                    int b = (0x000000ff & rgb);
                    int r2 = (0x00ff0000 & markerRGB) >> 16;
                    int g2 = (0x0000ff00 & markerRGB) >> 8;
                    int b2 = (0x000000ff & markerRGB);
                    r = (int) (r * (1.0 - rate) + r2 * rate);
                    g = (int) (g * (1.0 - rate) + g2 * rate);
                    b = (int) (b * (1.0 - rate) + b2 * rate);
                    return ((255 - transparency) << 24) | (r << 16) | (g << 8) | b;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
