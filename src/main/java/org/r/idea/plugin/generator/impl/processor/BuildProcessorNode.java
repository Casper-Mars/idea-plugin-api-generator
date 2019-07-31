package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.processor.ProcessorNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/7/31 22:22
 **/
public class BuildProcessorNode implements ProcessorNode<Context> {

    private String title = "building";

    /**
     * 执行处理
     *
     * @param context
     * @return
     */
    @Override
    public boolean doProcess(Context context) {
        context.setTitle(title);
        ConfigBean configurations = context.getConfigurations();
        if (configurations == null) {
            return false;
        }
        List<Node> interfaceNode = context.getInterfaceNode();
        if(CollectionUtils.isEmpty(interfaceNode)){
            return false;
        }



        return true;
    }
}
