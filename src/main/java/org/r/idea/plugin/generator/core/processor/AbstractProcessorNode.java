package org.r.idea.plugin.generator.core.processor;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:31
 **/
public abstract class AbstractProcessorNode<T> implements ProcessorNode<T> {


    private ProcessorNode<T> next;

    public ProcessorNode<T> addNext(ProcessorNode<T> next) {
        this.next = next;
        return next;
    }

    public ProcessorNode<T> getNext() {
        return next;
    }

    /**
     * 具体节点的处理过程
     *
     * @param context 上下文
     * @return
     */
    public abstract boolean process(T context);

    /**
     * 执行处理
     *
     * @param context 上下文
     * @return
     */
    @Override
    public boolean doProcess(T context) {

        if (context == null) {
            return false;
        }

        ProcessorNode<T> next = getNext();

        if (process(context) && next != null) {
            return next.doProcess(context);
        }

        return false;
    }


}
