package com.coderlan.demo.agent.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileClassLoader extends URLClassLoader {
    public JarFileClassLoader(File pluginJar, ClassLoader parent) throws IOException {
        super(new URL[]{pluginJar.toURI().toURL()}, parent);
    }
}
