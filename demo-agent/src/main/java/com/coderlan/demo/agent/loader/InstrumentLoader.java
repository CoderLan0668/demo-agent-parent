package com.coderlan.demo.agent.loader;

import com.coderlan.demo.agent.sdk.Instrument;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class InstrumentLoader {

    public static InstrumentHolder loadInstruments(File agentJarFile, List<File> pluginFileList) {
        InstrumentHolder instrumentHolder = new InstrumentHolder();
        instrumentHolder.withAgentJarFile(agentJarFile);
        pluginFileList.add(agentJarFile);
        for (File jar : pluginFileList) {
            ClassLoader jarCL = loadJar(jar);
            instrumentHolder.add(loadInstruments(jarCL), jarCL, jar);
        }
        return instrumentHolder;
    }

    private static ClassLoader loadJar(File jar) {
        try {
            return new JarFileClassLoader(jar, InstrumentLoader.class.getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Instrument> loadInstruments(ClassLoader cl) {
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        try {
            Enumeration<URL> resources = cl.getResources("META-INF/services/" + Instrument.class.getName());
            List<Instrument> instruments = new LinkedList<>();
            Set<String> implSet = getImplementations(resources);
            for (String impl : implSet) {
                Instrument instrument = instantiate(cl, impl);
                if (instrument != null) {
                    instruments.add(instrument);
                }
            }
            return instruments;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static Instrument instantiate(ClassLoader cl, String className) {
        try {
            return (Instrument) Class.forName(className, true, cl).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Set<String> getImplementations(Enumeration<URL> resources) throws IOException {
        Set<String> implementations = new LinkedHashSet<>();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            try (InputStream inputStream = url.openStream()) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while (reader.ready()) {
                    String line = reader.readLine().trim();
                    if (!line.isEmpty()) {
                        implementations.add(line);
                    }
                }
            }
        }
        return implementations;
    }
}
