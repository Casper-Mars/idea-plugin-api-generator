package org.r.idea.plugin.generator.impl.nodes;

import org.r.idea.plugin.generator.core.nodes.Node;

import java.util.List;

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
     * 当参数为泛型时的元素名
     */
    private List<String> genericityList;

    /**
     * 父类信息
     */
    private ParamNode superClass;

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

    public ParamNode() {
    }


    public void setRequired(boolean required) {
        isRequired = required;
    }


    public void setEntity(boolean entity) {
        isEntity = entity;
    }


    public void setGenericity(boolean genericity) {
        isGenericity = genericity;
    }


    public void setJson(boolean json) {
        isJson = json;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public boolean isJson() {
        return isJson;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isGenericity() {
        return isGenericity;
    }

    public boolean isRequired() {
        return isRequired;
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

    public List<String> getGenericityList() {
        return genericityList;
    }

    public void setGenericityList(List<String> genericityList) {
        this.genericityList = genericityList;
    }

    public ParamNode getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ParamNode superClass) {
        this.superClass = superClass;
    }

    @Override
    public ParamNode clone() {
        ParamNode paramNode;
        try {
            paramNode = (ParamNode) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            paramNode = new ParamNode();
            paramNode.setArray(this.isArray);
            paramNode.setEntity(this.isEntity);
            paramNode.setChildren(this.getChildren());
            paramNode.setTypeQualifiedName(this.getTypeQualifiedName());
            paramNode.setGenericity(this.isGenericity);
            paramNode.setGenericityList(this.getGenericityList());
            paramNode.setRequired(this.isRequired);
            paramNode.setJson(this.isJson);
            paramNode.setTypeShortName(this.getTypeShortName());
            paramNode.setDesc(this.getDesc());
            paramNode.setName(this.getName());
        }
        return paramNode;
    }
}
