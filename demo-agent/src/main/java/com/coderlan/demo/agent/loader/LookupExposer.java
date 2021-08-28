package com.coderlan.demo.agent.loader;

import java.lang.invoke.MethodHandles;

public class LookupExposer {

    public static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }
}
