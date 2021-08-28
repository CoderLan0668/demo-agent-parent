package com.coderlan.demo.agent.loader;

import com.coderlan.demo.agent.sdk.Instrument;

import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InstrumentHolder {

    private final Map<Instrument, ClassLoader> inst2CL = new HashMap<>();

    private final Map<String, ClassLoader> advice2CL = new HashMap<>();

    private final Map<ClassLoader, Set<String>> cl2Classes = new HashMap<>();

    public File getAgentJarFile() {
        return agentJarFile;
    }

    private File agentJarFile = null;

    public void add(List<Instrument> instruments, ClassLoader cl, File file) {
        for (Instrument inst : instruments) {
            inst2CL.put(inst, cl);
            advice2CL.put(inst.getAdviceClassName(), cl);
            Set<String> classes = cl2Classes.getOrDefault(cl, new LinkedHashSet<>());
            classes.addAll(scanForClasses(file));
            cl2Classes.put(cl, classes);
        }
    }

    public void withAgentJarFile(File agentJarFile) {
        this.agentJarFile = agentJarFile;
    }

    private Set<String> scanForClasses(File file) {
        Set<String> tempClasses = new LinkedHashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    tempClasses.add(jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return tempClasses;
    }

    public Set<Instrument> getInstruments() {
        return inst2CL.keySet();
    }

    public ClassLoader getClassLoader(Instrument inst) {
        return inst2CL.get(inst);
    }

    public ClassLoader getClassLoader(String adviceClassName) {
        return advice2CL.get(adviceClassName);
    }

    public Set<String> getClasses(ClassLoader cl) {
        return cl2Classes.get(cl);
    }

}
