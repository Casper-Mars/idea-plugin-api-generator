package org.r.idea.plugin.generator.gui.task;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.NotificationsManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon.Position;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.io.Compressor.Jar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.builder.DocBuilderImpl;
import org.r.idea.plugin.generator.impl.builder.JarBuilderImpl;
import org.r.idea.plugin.generator.impl.config.ConfigImpl;
import org.r.idea.plugin.generator.impl.parser.InterfaceParser;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName BuildTask
 * @Author Casper
 * @DATE 2019/6/12 12:21
 **/
public class BuildTask extends Task.Backgroundable {

    private String title;
    private Project project;
    private ProgressIndicator indicator;


    public BuildTask(@Nullable Project project,
        @Nls(capitalization = Capitalization.Title) @NotNull String title) {
        super(project, title);
        this.project = project;
        this.title = title;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        long start = System.currentTimeMillis();
        this.indicator = indicator;
        indicator.setIndeterminate(true);
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        /*获取配置*/
        Config config = getConfig();
        if (config == null) {
            JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder("配置有误，请确保接口目录和输出目录不为空", MessageType.INFO, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Position.atRight);
            return;
        }
        /*搜索接口文件*/
        List<PsiClass> psiClasses = searchAllInterface(config);
        /*转化接口*/
        List<Node> nodes = parseFile(Parser.getInstance(), psiClasses);
        /*生成文档源文件*/
        String srcDir = buildDoc(DocBuilder.getInstance(), nodes);
        /*生成jar包*/
        buildJar(JarBuilder.getInstance(), config.getWorkSpace(), srcDir);
        indicator.setFraction(1.0);
        indicator.setText("finish");

        long end = System.currentTimeMillis();
        JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder("finish:" + (end - start) + " ms", MessageType.INFO, null)
            .setFadeoutTime(7500)
            .createBalloon()
            .show(RelativePoint.getCenterOf(statusBar.getComponent()), Position.atRight);

    }

    private Config getConfig() {
        this.setTitle("init");
        StorageService storageService = StorageService.getInstance();
        if (storageService == null) {
            throw new RuntimeException("请先打开项目");
        }
        SettingState state = storageService.getState();
        if (state == null || project == null) {
            throw new RuntimeException("程序异常");
        }
        if (StringUtils.isEmpty(state.getInterfaceFilePaths()) || StringUtils.isEmpty(state.getOutputFilePaths())) {
            return null;
        }
        List<String> interfacePath = new ArrayList<>(
            Arrays.asList(state.getInterfaceFilePaths().split(Constants.SPLITOR)));
        Config config = new ConfigImpl(interfacePath, state.getOutputFilePaths(), state.getBaseClass(),
            state.getMarkdownFiles());
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
        Probe fileProbe = ServerManager.getServer(Probe.class);
        ApplicationManager.getApplication().runReadAction(() -> {
            allInterfaceClass.addAll(fileProbe.getAllInterfaceClass(config.getInterfaceFilesPath()));
        });
        updateProgress(0.1f);
        return allInterfaceClass;
    }

    private List<Node> parseFile(Parser parser, List<PsiClass> allInterfaceClass) {
        this.setTitle("parsing file");
        List<Node> interfaceNode = new ArrayList<>();
        ApplicationManager.getApplication().runReadAction(() -> {
            float total = allInterfaceClass.size();
            float count = 0;
            for (PsiClass target : allInterfaceClass) {
                try {
                    Node parse = parser.parse(target);
                    interfaceNode.add(parse);
                    count++;
                    updateProgress((count / total) * 0.4f);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return interfaceNode;
    }


    private String buildDoc(DocBuilder docBuilder, List<Node> interfaceNode) {
        this.setTitle("building");
        String srcDir = docBuilder.buildDocWithSaving(interfaceNode);
        updateProgress(0.2f);
        return srcDir;
    }

    public void buildJar(JarBuilder jarBuilder, String workSpace, String srcDir) {
        this.setTitle("generating");
        jarBuilder.buildJar(srcDir, workSpace);
        updateProgress(0.2f);
    }

    private void updateProgress(float f) {
        indicator.setFraction(indicator.getFraction() + f);
    }


}
