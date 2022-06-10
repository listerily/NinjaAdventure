package net.listerily.NinjaAdventure.resources;

import org.javatuples.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.function.Function;

public class CachedResources {
    private final ResourceManager resourceManager;
    public CachedResources(ResourceManager mgr) {
        resourceManager = mgr;
    }

    private final HashMap<String, Image> imageCache = new HashMap<>();
    public Image readImage(String name) throws IOException {
        synchronized (imageCache) {
            if (imageCache.containsKey(name)) {
                return imageCache.get(name);
            }
            Image image = ImageIO.read(resourceManager.getResourceURL(name));
            imageCache.put(name, image);
            return image;
        }
    }

    private final HashMap<Pair<Integer, String>, Font> fontCache = new HashMap<>();
    public Font readFont(int format, String name) throws IOException, FontFormatException {
        synchronized (fontCache) {
            Pair<Integer, String> keyPair = new Pair<>(format, name);
            if (fontCache.containsKey(keyPair)) {
                return fontCache.get(keyPair);
            }
            try (InputStream fileInputStream = resourceManager.openResource(name)) {
                Font font = Font.createFont(format, fileInputStream);
                fontCache.put(keyPair, font);
                return font;
            }
        }
    }

    private final HashMap<Object, Object> universalCache = new HashMap<>();
    public Object readUniversal(Object key, Function<Object, Object> func) {
        synchronized (universalCache) {
            if (universalCache.containsKey(key)) {
                return universalCache.get(key);
            }
            Object obj = func.apply(key);
            universalCache.put(key, obj);
            return obj;
        }
    }
}
