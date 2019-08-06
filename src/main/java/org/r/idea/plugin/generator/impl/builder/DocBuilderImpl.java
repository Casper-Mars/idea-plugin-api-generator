package org.r.idea.plugin.generator.impl.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.impl.nodes.MethodNode;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName DocBuilderImpl
 * @Author Casper
 * @DATE 2019/6/24 11:05
 **/
public class DocBuilderImpl implements DocBuilder {

    private InterfaceBuilder interfaceBuilder = new InterfaceBuilder();

    private EntityBuilder entityBuilder = new EntityBuilder();

    @Override
    public FileBO buildDoc(Node node) {
        String content = null;
        FileBO fileBO = new FileBO();
        if (node instanceof InterfaceNode) {
            content = interfaceBuilder.buildContent((InterfaceNode) node);
            fileBO.setName(node.getName());
        } else if (node instanceof ParamNode) {
            content = entityBuilder.buildContent((ParamNode) node);
            fileBO.setName(((ParamNode) node).getTypeShortName());
        }
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        fileBO.setContent(content);
        fileBO.setSuffix("java");

        return fileBO;
    }

    @Override
    public List<FileBO> buildDoc(List<Node> nodes) {
        List<FileBO> result = new ArrayList<>();
        for (Node node : nodes) {
            FileBO fileBO = buildDoc(node);
            if (fileBO != null) {
                result.add(fileBO);
            }
        }
        return result;
    }

    @Override
    public String buildDocWithSaving(List<Node> nodes,String workSpace) {
        List<FileBO> fileBOS = buildDoc(nodes);
        /*保存生成的文档*/
        return saveDoc(fileBOS, workSpace);
    }

    private String saveDoc(List<FileBO> docList, String workSpace) {
        if (CollectionUtils.isEmpty(docList)) {
            return null;
        }
        String filePrefix = workSpace + Constants.TMP_JAVA_DIR;
        Probe probe = Probe.getInstance();
        for (FileBO fileBO : docList) {
            if (StringUtils.isEmpty(fileBO.getPresentName())) {
                // TODO: 2019/6/24 文件名为空时应该记录下来
                continue;
            }
            probe.writerFile(filePrefix + fileBO.getPresentName(), fileBO.getContent());
        }
        return filePrefix;
    }

    private List<ParamNode> getAllEntity(InterfaceNode nodeList) {
        List<Node> method = nodeList.getChildren();
        List<Node> paramNodeList = new ArrayList<>();
        for (Node node : method) {
            MethodNode methodNode = (MethodNode) node;
            paramNodeList.add(methodNode.getResponed());
            paramNodeList.addAll(methodNode.getChildren());
        }
        List<ParamNode> result = new ArrayList<>();
        for (Node node : paramNodeList) {
            List<ParamNode> childEntity = getChildEntityFor((ParamNode) node);
            if (CollectionUtils.isNotEmpty(childEntity)) {
                result.addAll(childEntity);
            }
        }
        return result;
    }

    private List<ParamNode> getChildEntityFor(ParamNode node) {
        if (node == null || !node.isEntity()) {
            return null;
        }
        List<ParamNode> result = new ArrayList<>();
        result.add(node);
        for (Node child : node.getChildren()) {
            ParamNode paramNode = (ParamNode) child;
            if (paramNode.isEntity()) {
                List<ParamNode> entity = getChildEntityFor(paramNode);
                if (CollectionUtils.isNotEmpty(entity)) {
                    result.addAll(entity);
                }
            }
        }
        return result;
    }


}
