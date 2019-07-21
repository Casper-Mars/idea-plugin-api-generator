package org.r.idea.plugin.generator.gui.beans;

/**
 * @ClassName SettingState
 * @Author Casper
 * @DATE 2019/6/10 9:47
 **/
public class SettingState {

    /**
     * 持久化的接口文件路径
     */
    private String interfaceFilePaths;
    /**
     * 持久化的文件输出目录
     */
    private String outputFilePaths;
    /**
     * 持久化的基本类型
     */
    private String baseClass;

    /**
     * markdown文件的路径
     */
    private String markdownFiles;

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

    public String getInterfaceFilePaths() {
        return interfaceFilePaths;
    }

    public void setInterfaceFilePaths(String interfaceFilePaths) {
        this.interfaceFilePaths = interfaceFilePaths;
    }

    public String getOutputFilePaths() {
        return outputFilePaths;
    }

    public void setOutputFilePaths(String outputFilePaths) {
        this.outputFilePaths = outputFilePaths;
    }

    public String getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public String getMarkdownFiles() {
        return markdownFiles;
    }

    public void setMarkdownFiles(String markdownFiles) {
        this.markdownFiles = markdownFiles;
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

    public static SettingState getDefault() {
        SettingState settingState = new SettingState();
        settingState.setBaseClass("");
        settingState.setInterfaceFilePaths("");
        settingState.setOutputFilePaths("");
        settingState.setMarkdownFiles("");
        return settingState;
    }


}
