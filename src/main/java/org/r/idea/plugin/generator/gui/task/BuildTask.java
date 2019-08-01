package org.r.idea.plugin.generator.gui.task;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon.Position;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.ui.awt.RelativePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.exceptions.UplodaException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.ProcessorNode;
import org.r.idea.plugin.generator.core.upload.Deliveryman;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.builder.DocBuilderImpl;
import org.r.idea.plugin.generator.impl.builder.JarBuilderImpl;
import org.r.idea.plugin.generator.impl.config.ConfigImpl;
import org.r.idea.plugin.generator.impl.parser.InterfaceParser;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.impl.processor.*;
import org.r.idea.plugin.generator.impl.upload.DeliverymanImpl;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName BuildTask
 * @Author Casper
 * @DATE 2019/6/12 12:21
 **/
public class BuildTask extends Task.Backgroundable {

    private Project project;
    private ProgressIndicator indicator;

    private static final Logger LOG = Logger.getInstance(BuildTask.class);

    public BuildTask(@Nullable Project project,
                     @Nls(capitalization = Capitalization.Title) @NotNull String title) {
        super(project, title);
        this.project = project;
    }

//    @Override
//    public void run(@NotNull ProgressIndicator indicator) {
//        long start = System.currentTimeMillis();
//        this.indicator = indicator;
//        indicator.setIndeterminate(true);
//        indicator.setFraction(0.0f);
//        /*获取配置*/
//        Config config = getConfig();
//        if (config == null) {
//            showInfo("配置有误，请确保接口目录和输出目录不为空");
//            return;
//        }
//        /*搜索接口文件*/
//        List<PsiClass> psiClasses = searchAllInterface(config);
//        if (CollectionUtils.isEmpty(psiClasses)) {
//            showInfo("找不到接口文件");
//            return;
//        }
//        /*转化接口*/
//        List<Node> nodes = parseFile(Parser.getInstance(), psiClasses);
//        if (CollectionUtils.isEmpty(nodes)) {
//            showInfo("解析有误");
//            return;
//        }
//        /*生成文档源文件*/
//        String srcDir = buildDoc(DocBuilder.getInstance(), nodes);
//        /*生成jar包*/
//        buildJar(JarBuilder.getInstance(), config.getWorkSpace(), srcDir);
//        /*上传到服务器*/
//        uploadJar(config.getUsername(), config.getPassword(), config.getHost(), config.getRemotePath(), config.getWorkSpace());
//
//        indicator.setFraction(1.0);
//        indicator.setText("finish");
//
//        long end = System.currentTimeMillis();
//        showInfo("finish:" + (end - start) + " ms");
//    }

    private Config getConfig() {
        this.setTitle("init");
        StorageService storageService = StorageService.getInstance();
        if (storageService == null) {
            LOG.error("请先打开项目");
            showInfo("请先打开项目");
            return null;
        }
        SettingState state = storageService.getState();
        if (state == null || project == null) {
            LOG.error("程序异常");
            showInfo("程序异常");
            return null;
        }
        if (StringUtils.isEmpty(state.getInterfaceFilePaths()) || StringUtils.isEmpty(state.getOutputFilePaths())) {
            return null;
        }
        List<String> interfacePath = new ArrayList<>(
                Arrays.asList(state.getInterfaceFilePaths().split(Constants.SPLITOR)));
        Config config = new ConfigImpl(interfacePath, state.getOutputFilePaths(), state.getBaseClass(),
                state.getMarkdownFiles());
        /*设置上传资料*/
        if (StringUtils.isNotEmpty(state.getUsername()) &&
                StringUtils.isNotEmpty(state.getHost())) {
            ((ConfigImpl) config).setUsername(state.getUsername());
            ((ConfigImpl) config).setPassword(state.getPassword());
            ((ConfigImpl) config).setHost(state.getHost());
            ((ConfigImpl) config).setRemotePath(state.getRemotePath());
        }
        ConfigHolder.setConfig(config);
        /*注册探针服务*/
        ServerManager.registryServer(Probe.class, new FileProbe());
        /*注册parser服务*/
        ServerManager.registryServer(Parser.class, new InterfaceParser());
        /*注册文档生成服务*/
        ServerManager.registryServer(DocBuilder.class, new DocBuilderImpl());
        /*注册jar包生成服务*/
        ServerManager.registryServer(JarBuilder.class, new JarBuilderImpl());


        updateProgress(0.1f);
        return config;
    }

    private List<PsiClass> searchAllInterface(Config config) {
        this.setTitle("search file");
        List<PsiClass> allInterfaceClass = new ArrayList<>();
        Probe fileProbe = Probe.getInstance();
        ApplicationManager.getApplication().runReadAction(() -> {
            try {
                allInterfaceClass.addAll(fileProbe.getAllInterfaceClass(config.getInterfaceFilesPath()));
            } catch (ClassNotFoundException e) {
                LOG.error(e.getMsg());
                showInfo(e.getMsg());
            }
        });
        updateProgress(0.1f);
        return allInterfaceClass;
    }

    private List<Node> parseFile(Parser parser, List<PsiClass> allInterfaceClass) {
        this.setTitle("parsing file");
        List<Node> interfaceNode = new ArrayList<>();
        ApplicationManager.getApplication().runReadAction(() -> {
            float total = (1.0f / allInterfaceClass.size()) * 0.4f;
            for (PsiClass target : allInterfaceClass) {
                try {
                    Node parse = parser.parse(target);
                    interfaceNode.add(parse);
                    updateProgress(total);
                } catch (ClassNotFoundException e) {
                    LOG.error(e.getMsg());
                    showInfo(e.getMsg());
                }
            }
        });
        return interfaceNode;
    }


    private String buildDoc(DocBuilder docBuilder, List<Node> interfaceNode) {
        this.setTitle("building");
        String srcDir = docBuilder.buildDocWithSaving(interfaceNode, "");
        updateProgress(0.2f);
        return srcDir;
    }

    public void buildJar(JarBuilder jarBuilder, String workSpace, String srcDir) {
        this.setTitle("generating");
        jarBuilder.buildJar(srcDir, workSpace);
        updateProgress(0.1f);
    }

    private void updateProgress(float f) {
        indicator.setFraction(indicator.getFraction() + f);
    }

    private void showInfo(String info) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(info, MessageType.INFO, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Position.atRight);
    }


    private void uploadJar(String username, String password, String host, String remotePath, String workspace) {
        this.setTitle("uploading");
        String targetFile = workspace + "api-doc.jar";
        Integer port = 22;
        Deliveryman deliveryman = new DeliverymanImpl(targetFile, host, username, password, port, remotePath);
        try {
            deliveryman.doDeliver();
        } catch (UplodaException e) {
            e.printStackTrace();
            showInfo(e.getMsg());
        }
        updateProgress(0.1f);
    }


    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        long start = System.currentTimeMillis();
        StorageService storageService = StorageService.getInstance();
        if (storageService == null) {
            LOG.error("请先打开项目");
            showInfo("请先打开项目");
            return;
        }
        SettingState state = storageService.getState();
        if (state == null || project == null) {
            LOG.error("程序异常");
            showInfo("程序异常");
            return;
        }
        /*构建处理上下文*/
        Context context = new Context();
        context.setTask(this);
        context.setIndicator(indicator);
        context.setSettingState(state);
        /*构建处理链*/
        ProcessorNode<Context> processorNode = new ConfigProcessorNode();
        processorNode
                .addNext(new SearchProcessorNode())
                .addNext(new ParseProcessorNode())
                .addNext(new BuildProcessorNode())
                .addNext(new SaveFileProcessorNode())
                .addNext(new JarProcessorNode())
                .addNext(new UploadProcessorNode());

        try {
            processorNode.doProcess(context);
        } catch (UplodaException ue) {
            LOG.error(ue.getMsg());
            showInfo(ue.getMsg());
            ue.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            showInfo(e.getMessage());
        }

        long end = System.currentTimeMillis();
        showInfo("finish:" + (end - start) + " ms");
    }
}
