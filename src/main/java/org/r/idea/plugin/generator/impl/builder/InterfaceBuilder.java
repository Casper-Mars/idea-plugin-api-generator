package org.r.idea.plugin.generator.impl.builder;

import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.impl.nodes.MethodNode;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName InterfaceBuilder
 * @Author Casper
 * @DATE 2019/6/24 11:10
 **/
public class InterfaceBuilder {


    public String buildContent(InterfaceNode node) {

        /*获取接口模板*/
        String interfaceTemplate = TemplateProvider.getInterfaceTemplate();
        /*替换模板的内容*/
        interfaceTemplate = interfaceTemplate.replace("${DESCRIPTION}", node.getDesc())
            .replace("${NAME}", node.getName());
        StringBuilder method = new StringBuilder();
        for (Node child : node.getChildren()) {
            MethodNode methodNode = (MethodNode) child;
            String s = buildMethod(methodNode);
            if (StringUtils.isNotEmpty(s)) {
                method.append(s).append("\n");
            }
        }
        interfaceTemplate = interfaceTemplate.replace("${METHOD_LIST}", method.toString());
        return interfaceTemplate;
    }


    private String buildMethod(MethodNode node) {
        /*获取方法模板*/
        String methodTemplate = TemplateProvider.getMethodTemplate();
        /*替换模板的内容*/
        methodTemplate = methodTemplate.replace("${URL}", node.getUrl())
            .replace("${METHODTYPE}", node.getRequestType())
            .replace("${DESCRIPTION}", node.getDesc())
            .replace("${NAME}", node.getName());
        /*处理返回值*/
        ParamNode responed = (ParamNode) node.getResponed();
        methodTemplate = methodTemplate
            .replace("${RESPONSE_NAME}", responed.getTypeShortName() + (responed.isArray() ? "[]" : ""));
        /*处理参数：query参数和body参数*/
        StringBuilder body = new StringBuilder();
        StringBuilder query = new StringBuilder();
        boolean hasJson = false;
        for (Node param : node.getChildren()) {
            ParamNode paramNode = (ParamNode) param;
            /*判断请求参数有没有json格式的，决定contentType*/
            if (paramNode.isJson()) {
                hasJson = true;
            }
            body.append(getRequestBody(paramNode)).append(",");
            String queryStr = getQuery(paramNode);
            if (StringUtils.isNotEmpty(queryStr)) {
                query.append(queryStr);
            }
        }
        if (body.length() > 0) {
            body.deleteCharAt(body.length() - 1);
        }
        methodTemplate = methodTemplate.replace("${REQUEST_BODY}", body.toString())
            .replace("${PARAM_LIST}", query.toString())
            .replace("${CONTENT_TYPE}", hasJson ? "application/json" : "application/x-www-form-urlencoded");

        return methodTemplate;
    }

    private String getRequestBody(ParamNode node) {

        String name = node.getName();
        String type = node.isArray() ? node.getTypeShortName() + "[]" : node.getTypeShortName();
        if (node.isJson()) {
            return String.format("@RequestBody %s %s", type, name);
        } else {
            return String.format("%s %s", type, name);
        }
    }

    private String getQuery(ParamNode node) {

        if (node.isJson()) {
            return "";
        }
        if (node.isEntity()) {
            StringBuilder sb = new StringBuilder();
            for (Node child : node.getChildren()) {
                String query = getQuery((ParamNode) child);
                if (StringUtils.isNotEmpty(query)) {
                    sb.append(query);
                }
            }
            return sb.toString();
        } else {
            return String.format("@ApiImplicitParam(name = \"%s\", value = \"%s\", required = %s, dataType = \"%s\"),\n"
                , node.getName(), node.getDesc(), node.isRequired(),
                node.isArray() ? node.getTypeShortName() + "[]" : node.getTypeShortName());
        }
    }

}
