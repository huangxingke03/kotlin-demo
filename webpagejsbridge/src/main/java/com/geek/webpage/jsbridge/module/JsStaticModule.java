package com.geek.webpage.jsbridge.module;

/**
 * Created by pengwei on 2017/6/8.
 */

public abstract class JsStaticModule extends JsModule {

    private static final String STATIC_METHOD_NAME = "@static";

    @Override
    public final String getModuleName() {
        return STATIC_METHOD_NAME;
    }
}
