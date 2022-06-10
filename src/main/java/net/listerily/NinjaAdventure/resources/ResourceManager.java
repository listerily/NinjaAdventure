package net.listerily.NinjaAdventure.resources;

import net.listerily.NinjaAdventure.App;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

public class ResourceManager {
    private final ClassLoader classLoader;
    private final App app;

    private final CachedResources cachedResources;

    public ResourceManager(App app) {
        this.app = app;
        classLoader = getClass().getClassLoader();
        cachedResources = new CachedResources(this);
    }

    private Object warnNull(Object obj, String name) {
        if (obj == null)
            app.getAppLogger().log(Level.WARNING, "Resource Object " + name + " is null.");
        return obj;
    }

    public URL getResourceURL(String name) {
        return (URL)warnNull(classLoader.getResource(name), name);
    }

    public InputStream openResource(String name) {
        return (InputStream)warnNull(classLoader.getResourceAsStream(name), name);
    }

    public CachedResources getCachedResources() {
        return cachedResources;
    }
}
