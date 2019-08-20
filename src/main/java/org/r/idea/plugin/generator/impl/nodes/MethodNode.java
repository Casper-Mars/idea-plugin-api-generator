package org.r.idea.plugin.generator.impl.nodes;

import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName MethodNode
 * @Author Casper
 * @DATE 2019/6/21 17:38
 **/
public class MethodNode extends Node {

    /**
     * 请求响应内容
     */
    private Node responed;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 请求的url
     */
    private String url;

    /**
     * 请求接受的media type
     */
    private String consumes;

    /**
     * 请求返回的media type
     */
    private String produces;

    public String getConsumes() {
        return consumes;
    }

    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    public String getProduces() {
        return produces;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

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
