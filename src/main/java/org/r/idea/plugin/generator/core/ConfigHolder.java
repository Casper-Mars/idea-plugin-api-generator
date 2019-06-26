package org.r.idea.plugin.generator.core;

import org.r.idea.plugin.generator.core.config.Config;

/**
 * @ClassName ConfigHolder
 * @Author Casper
 * @DATE 2019/6/24 10:23
 **/
public class ConfigHolder {


    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config cur) {
        config = cur;
    }
}
