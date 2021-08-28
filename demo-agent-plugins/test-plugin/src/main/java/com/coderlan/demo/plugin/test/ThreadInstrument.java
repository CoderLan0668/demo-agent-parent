package com.coderlan.demo.plugin.test;

import com.coderlan.demo.agent.sdk.Instrument;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ThreadInstrument extends Instrument {

    @Advice.OnMethodEnter(inline = false)
    public static void onEnter(@Advice.This Thread thiz) {
        if (thiz.getName().contains("shouldInstrument")) {
            System.out.println("logs from ThreadInstrument in test plugin ...");
        }
    }

    @Override
    public ElementMatcher<? super TypeDescription> getTypeMatcher() {
        return named("java.lang.Thread");
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMethodMatcher() {
        return named("run");
    }
}
