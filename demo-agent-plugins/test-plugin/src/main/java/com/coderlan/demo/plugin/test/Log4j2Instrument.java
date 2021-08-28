package com.coderlan.demo.plugin.test;

import com.coderlan.demo.agent.sdk.Instrument;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.logging.log4j.core.LogEvent;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Log4j2Instrument extends Instrument {

    @Advice.OnMethodEnter(inline = false)
    public static void onEnter(@Advice.Argument(0) LogEvent logEvent) {
        System.out.println("instrumented: " + logEvent.getMessage().getFormattedMessage());
    }

    @Override
    public ElementMatcher<? super TypeDescription> getTypeMatcher() {
        return named("org.apache.logging.log4j.core.config.LoggerConfig");
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMethodMatcher() {
        return named("callAppenders");
    }
}
