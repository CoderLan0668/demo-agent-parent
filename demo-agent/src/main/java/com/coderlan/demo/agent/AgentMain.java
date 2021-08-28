package com.coderlan.demo.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class AgentMain {

    public static void premain(String agentArguments, Instrumentation inst) {
        try {
            File agentJar = getAgentJar();
            inst.appendToBootstrapClassLoaderSearch(new JarFile(agentJar));
            DemoAgent.init(inst, agentJar);
        } catch (Throwable t) {
            System.out.println("启动agent失败");
            t.printStackTrace();
        }
    }

    private static File getAgentJar() {
        try {
            return new File(AgentMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsoluteFile();
        } catch (Exception e) {
            throw new IllegalStateException("获取agent jar失败", e);
        }
    }
}
