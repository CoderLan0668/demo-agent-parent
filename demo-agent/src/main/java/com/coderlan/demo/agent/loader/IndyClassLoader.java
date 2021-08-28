package com.coderlan.demo.agent.loader;

import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;

import java.util.Map;

public class IndyClassLoader extends ByteArrayClassLoader.ChildFirst {

    public IndyClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions) {
        super(parent, true, typeDefinitions);
    }
}
