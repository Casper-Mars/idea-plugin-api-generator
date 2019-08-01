package org.r.idea.plugin.generator.gui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @ClassName TestFindClass
 * @Author Casper
 * @DATE 2019/6/20 16:51
 **/
public class TestFindClass extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {

        Project project = event.getProject();

        VirtualFile projectFile = project.getProjectFile();
        VirtualFile workspaceFile = project.getWorkspaceFile();

        Messages.showInfoMessage("完成", "提示");
    }


}
