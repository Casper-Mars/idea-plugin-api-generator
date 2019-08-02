package org.r.idea.plugin.generator.core.nodes;

import java.util.List;

/**
 * @ClassName Node
 * @Author Casper
 * @DATE 2019/6/21 10:17
 **/
public class Node implements Cloneable {

    /**
     * 名称
     */
    private String name = "";

    /**
     * 描述
     */
    private String desc = "";

    /**
     * 子节点
     */
    private List<Node> children;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
