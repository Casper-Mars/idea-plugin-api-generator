package org.r.idea.plugin.generator.gui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.local.CoreLocalFileSystem;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import gherkin.lexer.No;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.Generator;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.config.ConfigImpl;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.impl.parser.InterfaceParser;
import org.r.idea.plugin.generator.impl.parser.ParamParser;
import org.r.idea.plugin.generator.impl.probe.FileProbe;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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
