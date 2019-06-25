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

    public static SettingState getDefault() {
        SettingState settingState = new SettingState();
        settingState.setBaseClass("");
        settingState.setInterfaceFilePaths("");
        settingState.setOutputFilePaths("");
        return settingState;
    }


}
