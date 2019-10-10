package dev.arkav.sail.api;

import dev.arkav.openoryx.util.logging.Logger;
import dev.arkav.sail.impl.SailProxy;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginLoader {
    private Set<Class<? extends Plugin>> plugins;

    public void loadPlugins(String directoryName) {
        //noinspection unchecked
        this.plugins = (Set<Class<? extends Plugin>>) (Object) new Reflections(new ConfigurationBuilder().addUrls(
                Arrays.stream(Paths.get(System.getProperty("user.dir"), directoryName).toFile().listFiles())
                        .filter(t -> t.isFile() && t.getName().contains("jar"))
                        .map(t -> {
                            //noinspection unchecked
                            try {
                                return t.toURI().toURL();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collect(Collectors.toList())
        )).getTypesAnnotatedWith(SailPlugin.class);

        Logger.log("Sail", "Loaded " + this.plugins.size() + " plugin(s)");
    }

    public Plugin[] instantiatePlugins(SailProxy proxy) {
        return this.plugins.stream()
                .map(p -> {
                    try {
                        return p.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .peek(p -> p.initalize(proxy))
                .toArray(Plugin[]::new);
    }
}
