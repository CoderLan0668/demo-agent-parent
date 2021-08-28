package com.coderlan.demo.agent.sdk;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public abstract class Instrument {

    public abstract ElementMatcher<? super TypeDescription> getTypeMatcher();

    public abstract ElementMatcher<? super MethodDescription> getMethodMatcher();

    public String getAdviceClassName() {
        return getClass().getName();
    }
}
