package net.listerily.NinjaAdventure.resources;

import org.javatuples.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedInputStream;
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
    public synchronized Image readImage(String name) throws IOException {
        synchronized (imageCache) {
            if (imageCache.containsKey(name)) {
                return imageCache.get(name);
            }
            Image image = ImageIO.read(resourceManager.getResourceURL(name));
            imageCache.put(name, image);
            return image;
        }
    }

    private final HashMap<String, String> textCache = new HashMap<>();
    public synchronized String readText(String name) throws IOException {
        synchronized (textCache) {
            if (textCache.containsKey(name)) {
                return textCache.get(name);
            }
            StringBuilder stringBuilder = new StringBuilder();
            BufferedInputStream inputStream = new BufferedInputStream(resourceManager.openResource(name));
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead));
            }
            String result = stringBuilder.toString();
            textCache.put(name, result);
            return result;
        }
    }

    private final HashMap<Pair<Integer, String>, Font> fontCache = new HashMap<>();
    public synchronized Font readFont(int format, String name) throws IOException, FontFormatException {
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
    public synchronized Object readUniversal(Object key, Function<Object, Object> func) {
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
