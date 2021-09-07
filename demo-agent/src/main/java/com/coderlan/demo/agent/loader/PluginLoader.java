package com.coderlan.demo.agent.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class PluginLoader {

    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    public static List<File> loadPlugins() {
        String pluginsDirStr = System.getenv("PLUGINS_DIR");
        if (pluginsDirStr == null) {
            throw new IllegalStateException("请配置PLUGINS_DIR环境变量，值为项目根目录下plugins目录的路径");
        }
        File pluginDir = Paths.get(pluginsDirStr).toFile();
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
