package org.r.idea.plugin.generator.core.config;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * @ClassName ServerManager
 * @Author Casper
 * @DATE 2019/6/26 15:28
 **/
public class ServerManager {

    private Map<Class, Object> pool = new HashMap<>();

    private static ServerManager inner = new ServerManager();

    private ServerManager() {
    }

    private <T> T getService(@NotNull Class<T> clazz) {
        return (T) pool.get(clazz);
    }

    private void setService(@NotNull Class clazz, @NotNull Object target) {
        pool.put(clazz, target);
    }


    public static <T> T getServer(@NotNull Class<T> clazz) {
        return inner.getService(clazz);
    }

    public static void registryServer(@NotNull Class clazz, @NotNull Object target) {
        inner.setService(clazz, target);
    }

}
