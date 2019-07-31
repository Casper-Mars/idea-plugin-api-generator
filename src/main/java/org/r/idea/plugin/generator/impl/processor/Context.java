package org.r.idea.plugin.generator.impl.processor;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.psi.PsiClass;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.nodes.Node;
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
    private List<PsiClass> interfaceClass;

    /**
     * 接口节点
     */
    private List<Node> interfaceNode;


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

    public List<PsiClass> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(List<PsiClass> interfaceClass) {
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


}
