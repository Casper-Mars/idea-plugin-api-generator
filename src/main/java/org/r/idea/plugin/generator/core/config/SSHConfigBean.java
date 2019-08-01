package org.r.idea.plugin.generator.core.config;

public class SSHConfigBean {

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

    /**
     * 服务器端口
     */
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
