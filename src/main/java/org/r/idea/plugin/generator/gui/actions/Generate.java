package org.r.idea.plugin.generator.gui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.r.idea.plugin.generator.gui.task.BuildTask;

/**
 * @ClassName Generate
 * @Author Casper
 * @DATE 2019/6/10 9:37
 **/
public class Generate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();

        ProgressManager.getInstance().run(new BuildTask(project, "build doc"));


    }


}
