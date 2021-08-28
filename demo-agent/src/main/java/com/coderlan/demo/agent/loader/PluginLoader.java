package com.coderlan.demo.agent.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class PluginLoader {

    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    public static List<File> loadPlugins() {
        String pluginDirStr = "/Users/lan/demo/demo-agent-parent/plugins";
        File pluginDir = Paths.get(pluginDirStr).toFile();
        if (!pluginDir.exists()) {
            logger.warn("plugin dir not exists");
            return Collections.emptyList();
        }

        File[] pluginJars = pluginDir.listFiles((f, name) -> name.endsWith(".jar"));
        if (pluginJars == null) {
            return Collections.emptyList();
        }

        List<File> pluginJarList = new LinkedList<>();
        for (File pluginJar : pluginJars) {
            logger.info("Loading plugin {}", pluginJar.getName());
            pluginJarList.add(pluginJar);
        }
        return pluginJarList;
    }
}
