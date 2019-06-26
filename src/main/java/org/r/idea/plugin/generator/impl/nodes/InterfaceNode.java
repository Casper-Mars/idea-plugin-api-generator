package org.r.idea.plugin.generator.impl.nodes;

import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName InterfaceNode
 * @Author Casper
 * @DATE 2019/6/21 17:10
 **/
public class InterfaceNode extends Node {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
