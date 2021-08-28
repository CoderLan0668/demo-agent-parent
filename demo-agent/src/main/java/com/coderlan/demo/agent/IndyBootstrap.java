package com.coderlan.demo.agent;

import com.coderlan.demo.agent.loader.IndyClassLoader;
import com.coderlan.demo.agent.loader.InstrumentHolder;
import com.coderlan.demo.agent.loader.LookupExposer;
import net.bytebuddy.dynamic.ClassFileLocator;

import java.io.IOException;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndyBootstrap {

    private static final Method bsm;

    private static InstrumentHolder holder;

    public static final String LOOKUP_EXPOSER_CLASS = "com.coderlan.demo.agent.loader.LookupExposer";

    static {
        try {
            bsm = IndyBootstrap.class.getMethod("bootstrap",
                    MethodHandles.Lookup.class,
                    String.class,
                    MethodType.class,
                    String.class,
                    Object[].class);
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }
    }

    public static Method getBoostrapMethod(InstrumentHolder holder) {
        IndyBootstrap.holder = holder;
        return bsm;
    }

    public static ConstantCallSite bootstrap(MethodHandles.Lookup lookup,
                                             String adviceMethodName,
                                             MethodType adviceMethodType,
                                             String adviceClassName,
                                             Object... args) {
        try {
            ClassLoader callerCL = lookup.lookupClass().getClassLoader();
            ClassLoader adviceCL = holder.getClassLoader(adviceClassName);
            ClassFileLocator locator = new ClassFileLocator.Compound(
                    ClassFileLocator.ForClassLoader.of(adviceCL),
                    ClassFileLocator.ForJarFile.of(holder.getAgentJarFile()));
            Set<String> classNames = holder.getClasses(adviceCL);
            classNames.add(LOOKUP_EXPOSER_CLASS);
            IndyClassLoader indyClassLoader = new IndyClassLoader(
                    callerCL,
                    getTypeDefinitions(classNames, locator));

            Class<?> adviceClass = indyClassLoader.loadClass(adviceClassName);
            Class<LookupExposer> lookupExposer = (Class<LookupExposer>) indyClassLoader.loadClass(LOOKUP_EXPOSER_CLASS);
            MethodHandles.Lookup indyLookup = (MethodHandles.Lookup) lookupExposer.getMethod("getLookup").invoke(null);
            // When calling findStatic now, the lookup class will be one that is loaded by the plugin class loader
            MethodHandle methodHandle = indyLookup.findStatic(adviceClass, adviceMethodName, adviceMethodType);
            return new ConstantCallSite(methodHandle);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, byte[]> getTypeDefinitions(Set<String> classNames, ClassFileLocator locator) throws IOException {
        Map<String, byte[]> temp = new HashMap<>();
        for (String className : classNames) {
            temp.put(className, locator.locate(className).resolve());
        }
        return temp;
    }

}
