package com.coderlan.demo.agent;

import com.coderlan.demo.agent.loader.InstrumentHolder;
import com.coderlan.demo.agent.loader.InstrumentLoader;
import com.coderlan.demo.agent.loader.PluginLoader;
import com.coderlan.demo.agent.sdk.Instrument;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.jar.JarFile;

public class DemoAgent {

    private static final Logger logger = LoggerFactory.getLogger(DemoAgent.class);

    public static void init(Instrumentation inst, File agentJar) throws IOException {
        List<File> jarList = PluginLoader.loadPlugins();
        for (File file : jarList) {
            inst.appendToBootstrapClassLoaderSearch(new JarFile(file.getAbsoluteFile()));
        }

        InstrumentHolder holder = InstrumentLoader.loadInstruments(agentJar, jarList);
        for (Instrument instrument : holder.getInstruments()) {
            logger.info("loading advice: {} by CL: {}", instrument, instrument.getClass().getClassLoader());
        }
        applyAdvices(holder, inst);
    }

    public static void applyAdvices(InstrumentHolder holder, Instrumentation inst) {
        AgentBuilder builder = new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new AgentBuilder.Listener.Adapter() {
                    @Override
                    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .ignore(ElementMatchers.none());
        for (Instrument instrument : holder.getInstruments()) {
            Advice.WithCustomMapping withCustomMapping = Advice
                    .withCustomMapping()
                    .bootstrap(IndyBootstrap.getBoostrapMethod(holder));
            AgentBuilder.Transformer.ForAdvice transformer = new AgentBuilder.Transformer.ForAdvice(withCustomMapping)
                    .advice(instrument.getMethodMatcher(), instrument.getAdviceClassName())
                    .include(ClassLoader.getSystemClassLoader(), holder.getClassLoader(instrument))
                    .withExceptionHandler(net.bytebuddy.asm.Advice.ExceptionHandler.Default.PRINTING);
            builder = builder.type(instrument.getTypeMatcher())
                    .transform(transformer);
        }
        builder.installOn(inst);
    }
}
