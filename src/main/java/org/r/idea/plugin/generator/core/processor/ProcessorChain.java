package org.r.idea.plugin.generator.core.processor;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:24
 **/
public interface ProcessorChain<T> {


    /**
     * 执行链式操作
     */
    void process(T context);

}
