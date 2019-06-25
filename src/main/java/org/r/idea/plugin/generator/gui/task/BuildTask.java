package org.r.idea.plugin.generator.gui.task;

import com.intellij.openapi.application.ApplicationManager;
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
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.config.ConfigImpl;

/**
 * @ClassName BuildTask
 * @Author Casper
 * @DATE 2019/6/12 12:21
 **/
public class BuildTask extends Task.Backgroundable {

    private String title;
    private Project project;


    public BuildTask(@Nullable Project project,
        @Nls(capitalization = Capitalization.Title) @NotNull String title) {
        super(project, title);
        this.project = project;
        this.title = title;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        long start = System.currentTimeMillis();
        indicator.setIndeterminate(true);
        StorageService storageService = StorageService.getInstance();
        SettingState state = storageService.getState();
        double precent = 0.0;
        if (state == null || project == null) {
            throw new RuntimeException("程序异常");
        }
        precent += 0.1;
        indicator.setFraction(precent);
        this.setTitle("search file");
        Config config = getConfig(state);
        AtomicReference<List<PsiClass>> allInterfaceClass = new AtomicReference<>();
        ApplicationManager.getApplication().runReadAction(
            () -> allInterfaceClass.set(config.getFileProbe().getAllInterfaceClass(config.getInterfaceFilesPath())));
        precent += 0.1;
        indicator.setFraction(precent);
        this.setTitle("parsing file");
        List<Node> interfaceNode = new ArrayList<>();
        ApplicationManager.getApplication().runReadAction(() -> {
            double total = allInterfaceClass.get().size();
            double count = 0;
            double cur = indicator.getFraction();
            for (PsiClass target : allInterfaceClass.get()) {
                try {
                    Node parse = config.getInterfaceParser().parse(target);
                    interfaceNode.add(parse);
                    count++;
                    cur += (count / total) * 0.4;
                    indicator.setFraction(cur);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        this.setTitle("building");
        List<FileBO> docList = config.getDocBuilder().buildDoc(interfaceNode);
        precent += 0.2;
        indicator.setFraction(precent);
        this.setTitle("generating");
        String srcDir = config.getFileProbe().saveDoc(docList, config.getWorkSpace());
        config.getJarBuilder().buildJar(srcDir, config.getWorkSpace());
        indicator.setFraction(1.0);
        indicator.setText("finish");

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        long end = System.currentTimeMillis();
        JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder("finish:" + (end - start) + " ms", MessageType.INFO, null)
            .setFadeoutTime(7500)
            .createBalloon()
            .show(RelativePoint.getCenterOf(statusBar.getComponent()), Position.atRight);

    }

    private Config getConfig(SettingState state) {

        List<String> interfacePath = new ArrayList<>(
            Arrays.asList(state.getInterfaceFilePaths().split(Constants.SPLITOR)));
        Config config = new ConfigImpl(interfacePath, state.getOutputFilePaths(), state.getBaseClass());
        ConfigHolder.setConfig(config);
        return config;
    }

    private List<PsiClass> searchAllInterface(Config config, ProgressIndicator indicator) {
        double cur = indicator.getFraction();
        AtomicReference<List<PsiClass>> allInterfaceClass = new AtomicReference<>();
        ApplicationManager.getApplication().runReadAction(() -> {
            allInterfaceClass.set(config.getFileProbe().getAllInterfaceClass(config.getInterfaceFilesPath()));
        });
        cur += 0.2;
        indicator.setFraction(cur);
        return allInterfaceClass.get();
    }


}
