package org.r.idea.plugin.generator.core.processor;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:24
 **/
public interface ProcessorNode<T> {


    /**
     * 执行处理
     *
     * @param context
     * @return
     */
    boolean doProcess(T context);

    /**
     * 添加下一个节点
     *
     * @param next 下一个节点
     * @return
     */
    ProcessorNode<T> addNext(ProcessorNode<T> next);

}
