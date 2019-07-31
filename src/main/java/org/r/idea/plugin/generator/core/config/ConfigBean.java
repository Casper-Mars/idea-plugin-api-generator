package org.r.idea.plugin.generator.core.config;

import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:43
 **/
public class ConfigBean {


    /**
     * 接口文件路径
     */
    private List<String> interfaceFilePaths;

    /**
     * 工作空间
     */
    private String workSpace;

    /**
     * 额外的markdown文件路径
     */
    private String markdownPath;

    /**
     * 是否debug模式
     */
    private boolean isDebug;

    /**
     * 基础类
     */
    private String[] baseClass = {"String", "Long", "int", "long", "char", "Integer", "double", "Double",
            "BigDecimal", "LocalDateTime", "BigDecimal", "boolean", "Boolean", "BindingResult", "Date", "byte", "Byte"
    };

    /**
     * 服务器用户名
     */
    private String username;

    /**
     * 服务器密码
     */
    private String password;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器远端路径
     */
    private String remotePath;

    public List<String> getInterfaceFilePaths() {
        return interfaceFilePaths;
    }

    public void setInterfaceFilePaths(List<String> interfaceFilePaths) {
        this.interfaceFilePaths = interfaceFilePaths;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }

    public String getMarkdownPath() {
        return markdownPath;
    }

    public void setMarkdownPath(String markdownPath) {
        this.markdownPath = markdownPath;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String[] getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(String[] baseClass) {
        this.baseClass = baseClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
}
