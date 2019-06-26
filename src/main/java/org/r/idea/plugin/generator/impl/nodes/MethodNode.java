package org.r.idea.plugin.generator.impl.nodes;

import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName MethodNode
 * @Author Casper
 * @DATE 2019/6/21 17:38
 **/
public class MethodNode extends Node {


    private Node responed;

    private String requestType;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Node getResponed() {
        return responed;
    }

    public void setResponed(Node responed) {
        this.responed = responed;
    }
}
