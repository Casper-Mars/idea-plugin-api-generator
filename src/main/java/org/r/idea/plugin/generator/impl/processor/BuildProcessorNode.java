package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.core.processor.ProcessorNode;
import org.r.idea.plugin.generator.impl.builder.DocBuilderImpl;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/7/31 22:22
 **/
public class BuildProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "building";

    /**
     * 具体节点的处理过程
     *
     * @param context 上下文
     * @return
     */
    @Override
    public boolean process(Context context) {
        context.setTitle(title);
        ConfigBean configurations = context.getConfigurations();
        if (configurations == null) {
            return false;
        }
        List<Node> interfaceNode = context.getInterfaceNode();
        if (CollectionUtils.isEmpty(interfaceNode)) {
            return false;
        }

        List<Node> entityNode = context.getEntityNode();


        DocBuilder docBuilder = new DocBuilderImpl();
        List<FileBO> fileBOS = docBuilder.buildDoc(interfaceNode);
        fileBOS.addAll(docBuilder.buildDoc(entityNode));
        context.setFileBOS(fileBOS);
        context.updateProgress(0.1f);

        return true;
    }

}
