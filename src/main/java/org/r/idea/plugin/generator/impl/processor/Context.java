package org.r.idea.plugin.generator.impl.processor;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.gui.beans.SettingState;

import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:40
 **/
public class Context {


    /**
     * 配置面板参数
     */
    private SettingState settingState;

    /**
     * 进度条指示器
     */
    private ProgressIndicator indicator;

    /**
     * 执行当前链的后台任务
     */
    private Task.Backgroundable task;

    /**
     * 配置
     */
    private ConfigBean configurations;

    /**
     * 接口类
     */
    private List<PsiElement> interfaceClass;

    /**
     * 接口节点
     */
    private List<Node> interfaceNode;

    /**
     * 实体类
     */
    private List<Node> entityNode;

    /**
     * 产生的源文件的路径
     */
    private String srcDir;

    /**
     * 需要上传的jar包的全路径
     */
    private String targetJarFile;

    /**
     * 文件属性
     */
    private List<FileBO> fileBOS;

    /**
     * 文件探针，处理文件操作
     */
    private Probe fileProbe;

    public Probe getFileProbe() {
        return fileProbe;
    }

    public void setFileProbe(Probe fileProbe) {
        this.fileProbe = fileProbe;
    }

    public List<FileBO> getFileBOS() {
        return fileBOS;
    }

    public void setFileBOS(List<FileBO> fileBOS) {
        this.fileBOS = fileBOS;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getTargetJarFile() {
        return targetJarFile;
    }

    public void setTargetJarFile(String targetJarFile) {
        this.targetJarFile = targetJarFile;
    }

    public List<Node> getInterfaceNode() {
        return interfaceNode;
    }

    public void setInterfaceNode(List<Node> interfaceNode) {
        this.interfaceNode = interfaceNode;
    }

    public SettingState getSettingState() {
        return settingState;
    }

    public void setSettingState(SettingState settingState) {
        this.settingState = settingState;
    }

    public ConfigBean getConfigurations() {
        return configurations;
    }

    public void setConfigurations(ConfigBean configurations) {
        this.configurations = configurations;
    }

    public List<PsiElement> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(List<PsiElement> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public ProgressIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(ProgressIndicator indicator) {
        this.indicator = indicator;
    }

    public Task.Backgroundable getTask() {
        return task;
    }

    public void setTask(Task.Backgroundable task) {
        this.task = task;
    }

    public void setTitle(String title) {
        if (task != null) {
            task.setTitle(title);
        }
    }

    public void updateProgress(float f) {
        if (indicator != null) {
            indicator.setFraction(indicator.getFraction() + f);
        }
    }

    public List<Node> getEntityNode() {
        return entityNode;
    }

    public void setEntityNode(List<Node> entityNode) {
        this.entityNode = entityNode;
    }
}
