package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.config.SSHConfigBean;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Casper
 * @DATE 2019/7/31 20:55
 **/
public class ConfigProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "init";

    /**
     * 具体节点的处理过程
     *
     * @param context 上下文
     * @return
     */
    @Override
    public boolean process(Context context) {
        context.setTitle(title);
        SettingState state = context.getSettingState();
        ConfigBean configBean = new ConfigBean();
        List<String> interfacePath = new ArrayList<>(
                Arrays.asList(state.getInterfaceFilePaths().split(Constants.SPLITOR)));
        configBean.setInterfaceFilePaths(interfacePath.stream().map(this::formatPath).collect(Collectors.toList()));
        configBean.setMarkdownPath(formatPath(state.getMarkdownFiles()));
        configBean.setWorkSpace(formatPath(state.getOutputFilePaths()));
        String baseClass = state.getBaseClass();
        if (StringUtils.isNotEmpty(baseClass)) {
            Utils.baseClass = merge(baseClass.split(Constants.SPLITOR), configBean.getBaseClass());
        } else {
            Utils.baseClass = configBean.getBaseClass();
            configBean.setBaseClass(Utils.baseClass);
        }
        /*设置上传资料*/
        if (StringUtils.isNotEmpty(state.getUsername()) &&
                StringUtils.isNotEmpty(state.getHost())) {
            SSHConfigBean sshConfigBean = new SSHConfigBean();
            sshConfigBean.setUsername(state.getUsername());
            sshConfigBean.setPassword(state.getPassword());
            sshConfigBean.setHost(state.getHost());
            sshConfigBean.setRemotePath(formatPath(state.getRemotePath()));
            sshConfigBean.setPort(22);
            configBean.setSshConfigBean(sshConfigBean);
        }
        configBean.setDebug(true);
        Probe fileProbe = new FileProbe();
        /*设置文件探针*/
        context.setFileProbe(fileProbe);
        context.setConfigurations(configBean);
        context.updateProgress(0.1f);
        return true;
    }

    /**
     * 格式化路径
     *
     * @param path
     * @return
     */
    private String formatPath(String path) {
        if (StringUtils.isEmpty(path) || path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }

    /**
     * 去重性合并两个数组
     *
     * @param arr1
     * @param arr2
     * @return
     */
    private String[] merge(String[] arr1, String[] arr2) {
        List<String> list1 = new ArrayList<>(Arrays.asList(arr1));
        List<String> list2 = new ArrayList<>(Arrays.asList(arr2));

        list1.addAll(list2);
        return list1.stream().distinct().filter(t -> !StringUtils.isEmpty(t)).toArray(String[]::new);
    }


}
