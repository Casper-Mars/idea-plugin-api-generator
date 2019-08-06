package org.r.idea.plugin.generator.impl.builder;

import org.jdom2.Element;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.impl.nodes.MethodNode;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.XmlTag;

import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/8/6 21:05
 **/
public class XmlBuilder {


    public Element buildContent(List<Node> node) {

        if (CollectionUtils.isEmpty(node)) {
            return null;
        }
        Element rootElement = buildRoot("", "");

        for (Node child : node) {
            if (!(child instanceof InterfaceNode)) {
                throw new RuntimeException("build fail due to parse wrong");
            }
            Element subGroup = buildSubGroup((InterfaceNode) child);
            rootElement.addContent(subGroup);
        }
        return rootElement;
    }


    /**
     * 生成根节点
     *
     * @param prefix 路径前准
     * @param desc   描述
     * @return
     */
    private Element buildRoot(String prefix, String desc) {

        Element root = new Element(XmlTag.GROUP_TAG);
        root.setAttribute(XmlTag.DESC_ATTR, desc);
        root.setAttribute(XmlTag.PREFIX_ATTR, prefix);
        return root;
    }


    private Element buildSubGroup(InterfaceNode node) {
        Element root = new Element(XmlTag.SUB_GROUP_TAG);
        root.setAttribute(XmlTag.DESC_ATTR, node.getDesc());
        root.setAttribute(XmlTag.PREFIX_ATTR, node.getBaseUrl());
        if (CollectionUtils.isNotEmpty(node.getChildren())) {
            for (Node child : node.getChildren()) {
                Element item = buildItem((MethodNode) child);
                root.addContent(item);
            }
        }
        return root;
    }


    private Element buildItem(MethodNode methodNode) {

        Element item = new Element(XmlTag.ITEM_TAG);
        /*url标签*/
        Element url = new Element(XmlTag.URL_TAG);
        url.setText(methodNode.getUrl());
        item.addContent(url);

        /*method标签*/
        Element method = new Element(XmlTag.METHOD_TAG);
        method.setText(methodNode.getRequestType());
        item.addContent(method);

        /*desc标签*/
        Element desc = new Element(XmlTag.DESC_TAG);
        desc.setText(methodNode.getDesc());
        item.addContent(desc);

        /*consumes标签*/
        Element consumes = new Element(XmlTag.CONSUMES_TAG);
        consumes.setText("");
        item.addContent(consumes);

        /*produces标签*/
        Element produces = new Element(XmlTag.PRODUCES_TAG);
        produces.setText("");
        item.addContent(produces);


        /*parameters标签*/
        Element parameters = new Element(XmlTag.PARAMETERS_TAG);
        boolean isJson = false;
        if (CollectionUtils.isNotEmpty(methodNode.getChildren())) {
            for (Node child : methodNode.getChildren()) {
                ParamNode node = (ParamNode) child;
                if (!isJson) {
                    isJson = node.isJson();
                }
                Element param = buildParam(node);
                parameters.addContent(param);
            }
        }
        parameters.setAttribute(XmlTag.ISJSON_ATTR, isJson ? "true" : "false");
        item.addContent(parameters);

        /*resp标签*/
        Element resp = new Element(XmlTag.RESP_TAG);
        if (methodNode.getResponed() != null) {
            resp.addContent(buildParam((ParamNode) methodNode.getResponed()));
        }
        item.addContent(resp);

        return item;
    }


    private Element buildParam(ParamNode paramNode) {

        Element param = new Element(XmlTag.PARAMETER_TAG);

        param.setAttribute(XmlTag.ISREQUIRE_ATTR, paramNode.isRequired() ? "true" : "false");
        param.setAttribute(XmlTag.ISJSON_ATTR, paramNode.isJson() ? "true" : "false");
        param.setAttribute(XmlTag.ISENTITY_ATTR, paramNode.isEntity() ? "true" : "false");

        /*name标签*/
        Element name = new Element(XmlTag.NAME_TAG);
        name.setText(paramNode.getName());
        param.addContent(name);

        /*type标签*/
        Element type = new Element(XmlTag.TYPE_TAG);
        type.setText(paramNode.getTypeShortName() + (paramNode.isArray() ? "[]" : ""));
        param.addContent(type);

        /*desc标签*/
        Element desc = new Element(XmlTag.DESC_TAG);
        desc.setText(paramNode.getDesc());
        param.addContent(desc);

        if (CollectionUtils.isNotEmpty(paramNode.getChildren())) {
            /*children标签*/
            Element children = new Element(XmlTag.CHILDREN_TAG);
            paramNode.getChildren().forEach(t -> {
                Element element = buildParam((ParamNode) t);
                children.addContent(element);
            });
            param.addContent(children);
        }
        return param;
    }


}
