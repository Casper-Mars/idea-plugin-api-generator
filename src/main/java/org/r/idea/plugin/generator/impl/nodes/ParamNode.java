package org.r.idea.plugin.generator.impl.nodes;

import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName ParamNode
 * @Author Casper
 * @DATE 2019/6/21 10:18
 **/
public class ParamNode extends Node {

    /**
     * 类型简称
     */
    private String typeShortName;

    /**
     * 类型全称
     */
    private String typeQualifiedName;

    /**
     * 是否实体
     */
    private boolean isEntity;
    /**
     * 请求时是否为json格式
     */
    private boolean isJson;

    /**
     * 请求时是否为数组
     */
    private boolean isArray;

    /**
     * 是否泛型
     */
    private boolean isGenericity;
    /**
     * 是否必传
     */
    private boolean isRequired;

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public void setEntity(boolean entity) {
        isEntity = entity;
    }

    public boolean isGenericity() {
        return isGenericity;
    }

    public void setGenericity(boolean genericity) {
        isGenericity = genericity;
    }

    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean json) {
        isJson = json;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public String getTypeShortName() {
        return typeShortName;
    }

    public void setTypeShortName(String typeShortName) {
        this.typeShortName = typeShortName;
    }

    public String getTypeQualifiedName() {
        return typeQualifiedName;
    }

    public void setTypeQualifiedName(String typeQualifiedName) {
        String[] split = typeQualifiedName.split("\\.");
        if (split.length != 0) {
            this.typeShortName = split[split.length - 1];
        } else {
            this.typeShortName = typeQualifiedName;
        }
        this.typeQualifiedName = typeQualifiedName;
    }

}
