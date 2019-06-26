package org.r.idea.plugin.generator.impl.builder;

import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName EntityBuilder
 * @Author Casper
 * @DATE 2019/6/24 12:59
 **/
public class EntityBuilder {


    public String buildContent(ParamNode node) {

        /*获取实体模板*/
        String entityTemplate = TemplateProvider.getEntityTemplate();
        entityTemplate = entityTemplate.replace("${NAME}", node.getTypeShortName());
        StringBuilder field = new StringBuilder();
        StringBuilder method = new StringBuilder();
        for (Node child : node.getChildren()) {
            field.append(getField((ParamNode) child));
            method.append(getMethod((ParamNode) child));
        }
        entityTemplate = entityTemplate.replace("${FIELDS}", field.toString())
            .replace("${METHODS}", method.toString());
        return entityTemplate;
    }


    private String getField(ParamNode node) {
        /*获取模板*/
        String entityFieldTemplate = TemplateProvider.getEntityFieldTemplate();
        entityFieldTemplate = entityFieldTemplate.replace("${COMMENT}", node.getDesc())
            .replace("${FIELD_TYPE}", node.isArray() ? node.getTypeShortName() + "[]" : node.getTypeShortName())
            .replace("${FIELD_NAME}", node.getName());
        return entityFieldTemplate;
    }

    private String getMethod(ParamNode node) {
        StringBuilder method = new StringBuilder();
        String type = node.isArray() ? node.getTypeShortName() + "[]" : node.getTypeShortName();
        String name = node.getName();
        String caseName = StringUtils.upperFirstChart(node.getName());
        /*获取setter模板*/
        String entitySetterTemplate = TemplateProvider.getEntitySetterTemplate();
        entitySetterTemplate = entitySetterTemplate.replace("${FIELD_NAME}", name)
            .replace("${FIELD_TYPE}", type)
            .replace("${CASE_FIELD_NAME}", caseName);
        method.append(entitySetterTemplate).append("\n");
        /*获取getter模板*/
        String entityGetterTemplate = TemplateProvider.getEntityGetterTemplate();
        entityGetterTemplate = entityGetterTemplate.replace("${FIELD_NAME}", name)
            .replace("${FIELD_TYPE}", type)
            .replace("${CASE_FIELD_NAME}", caseName);
        method.append(entityGetterTemplate).append("\n");
        return method.toString();
    }

}
