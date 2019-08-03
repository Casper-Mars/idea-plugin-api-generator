package org.r.idea.plugin.generator.impl.processor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.core.processor.ProcessorNode;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.impl.parser.EntityContainer;
import org.r.idea.plugin.generator.impl.parser.InterfaceParser;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Casper
 * @DATE 2019/7/31 22:13
 **/
public class ParseProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "parsing file";

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
        List<PsiClass> interfaceClass = context.getInterfaceClass();
        if (CollectionUtils.isEmpty(interfaceClass)) {
            return false;
        }
        List<Node> interfaceNode = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Parser parser = new InterfaceParser();
        ApplicationManager.getApplication().runReadAction(() -> {
            float total = (1.0f / interfaceClass.size()) * 0.4f;
            for (PsiClass target : interfaceClass) {
                try {
                    System.out.println(target.getQualifiedName());
                    Node parse = parser.parse(target);
                    interfaceNode.add(parse);
                    context.updateProgress(total);
                } catch (ClassNotFoundException e) {
                    sb.append(e.getMsg());
                }
            }
        });
        if (sb.length() != 0) {
            throw new RuntimeException(sb.toString());
        }
        context.setInterfaceNode(interfaceNode);
        /*拷贝实体容器中的实体*/
        List<ParamNode> allValues = EntityContainer.getAllValues();
        List<Node> target = new ArrayList<>();
        allValues.forEach(t -> target.add(t.clone()));
        context.setEntityNode(target);
//        context.setEntityNode(EntityContainer.getAllValues().stream().map(t -> (Node) t).collect(Collectors.toList()));
        return true;
    }

}
