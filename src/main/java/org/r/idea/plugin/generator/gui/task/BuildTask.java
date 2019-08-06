package org.r.idea.plugin.generator.gui.task;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon.Position;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.core.exceptions.UplodaException;
import org.r.idea.plugin.generator.core.processor.ProcessorNode;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;
import org.r.idea.plugin.generator.impl.processor.*;

/**
 * @ClassName BuildTask
 * @Author Casper
 * @DATE 2019/6/12 12:21
 **/
public class BuildTask extends Task.Backgroundable {

    private Project project;
    private static final Logger LOG = Logger.getInstance(BuildTask.class);

    public BuildTask(@Nullable Project project,
                     @Nls(capitalization = Capitalization.Title) @NotNull String title) {
        super(project, title);
        this.project = project;
    }

    private void showInfo(String info) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(info, MessageType.INFO, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Position.atRight);
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
//                .addNext(new SaveFileProcessorNode())
//                .addNext(new JarProcessorNode())
//                .addNext(new UploadProcessorNode())
        ;

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
