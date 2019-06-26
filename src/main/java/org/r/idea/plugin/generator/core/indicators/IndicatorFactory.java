package org.r.idea.plugin.generator.core.indicators;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.r.idea.plugin.generator.impl.indicators.GenericityIndicatorImpl;
import org.r.idea.plugin.generator.impl.indicators.InterfaceIndicatorImpl;

/**
 * @ClassName IndicatorFactory
 * @Author Casper
 * @DATE 2019/6/21 15:10
 **/
public class IndicatorFactory {

    private Map<Class, Object> pool = new HashMap<>();

    private static IndicatorFactory indicatorFactory = new IndicatorFactory();

    public void setIndicator(@NotNull Class clazz, @NotNull Object target) {
        pool.put(clazz, target);
    }

    public <T> T getIndicator(@NotNull Class<T> clazz) {
        return (T) pool.get(clazz);
    }

    static {
        indicatorFactory.setIndicator(GenericityIndicator.class, new GenericityIndicatorImpl());
        indicatorFactory.setIndicator(InterfaceIndicator.class, new InterfaceIndicatorImpl());
    }

    public static void set(@NotNull Class clazz, @NotNull Object target) {
        indicatorFactory.setIndicator(clazz, target);
    }

    public static <T> T get(@NotNull Class<T> clazz) {
        return indicatorFactory.getIndicator(clazz);
    }

}
