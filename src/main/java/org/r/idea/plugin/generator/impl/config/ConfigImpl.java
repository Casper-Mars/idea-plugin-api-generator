package org.r.idea.plugin.generator.impl.config;

import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ConfigImpl
 * @Author Casper
 * @DATE 2019/6/24 13:49
 **/
public class ConfigImpl implements Config {


    private List<String> interfaceFilePaths;

    private String workSpace;

    private String markdownPath;

    private boolean isDebug;

    private String[] baseClass = {"String", "Long", "int", "long", "char", "Integer", "double", "Double",
            "BigDecimal", "LocalDateTime", "BigDecimal", "boolean", "Boolean", "BindingResult", "Date"
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


    public ConfigImpl(List<String> interfaceFilePaths, String workSpace, String baseClass, String markdownPath) {
        init(interfaceFilePaths, workSpace, baseClass, markdownPath, false);
    }

    public ConfigImpl(List<String> interfaceFilePaths, String workSpace, String baseClass, String markdownPath,
                      boolean isDebug) {
        init(interfaceFilePaths, workSpace, baseClass, markdownPath, isDebug);
    }


    private void init(List<String> interfaceFilePaths, String workSpace, String baseClass,
                      String markdownPath, boolean isDebug) {
        this.isDebug = isDebug;
        this.markdownPath = formatPath(markdownPath);
        setBaseClass(baseClass);
        this.interfaceFilePaths = interfaceFilePaths.stream().map(this::formatPath).collect(Collectors.toList());
        this.workSpace = formatPath(workSpace);
    }


    public void setBaseClass(String baseClass) {
        if (StringUtils.isNotEmpty(baseClass)) {
            Utils.baseClass = merge(baseClass.split(Constants.SPLITOR), this.baseClass);
        } else {
            Utils.baseClass = this.baseClass;
        }
    }

    private String[] merge(String[] arr1, String[] arr2) {
        /*去重性合并两个数组*/
        List<String> list1 = new ArrayList<>(Arrays.asList(arr1));
        List<String> list2 = new ArrayList<>(Arrays.asList(arr2));

        list1.addAll(list2);
        return list1.stream().distinct().filter(t -> !StringUtils.isEmpty(t)).toArray(String[]::new);
    }

    @Override
    public List<String> getInterfaceFilesPath() {
        return interfaceFilePaths;
    }

    @Override
    public String getWorkSpace() {
        return workSpace;
    }

    @Override
    public String getMarkdownPath() {
        return markdownPath;
    }

    @Override
    public boolean isDebug() {
        return this.isDebug;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getRemotePath() {
        return remotePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    private String formatPath(String path) {
        if (StringUtils.isEmpty(path) || path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }


}
