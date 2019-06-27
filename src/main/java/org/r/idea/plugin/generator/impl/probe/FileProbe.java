package org.r.idea.plugin.generator.impl.probe;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.local.CoreLocalFileSystem;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;

import com.intellij.psi.impl.source.PsiJavaFileImpl;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.indicators.InterfaceIndicator;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.indicators.InterfaceIndicatorImpl;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName Probe
 * @Author Casper
 * @DATE 2019/6/22 12:39
 **/
public class FileProbe implements Probe {


    private InterfaceIndicator interfaceIndicator = InterfaceIndicator.getInstance();

    /**
     * 获取全部的接口文件
     *
     * @param interfaceFilePath 接口文件目录
     */
    @Override
    public List<PsiClass> getAllInterfaceClass(List<String> interfaceFilePath) throws ClassNotFoundException {
        CoreLocalFileSystem coreLocalFileSystem = new CoreLocalFileSystem();
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        List<PsiClass> result = new ArrayList<>();
        for (String path : interfaceFilePath) {
            VirtualFile pathFile = coreLocalFileSystem.findFileByPath(path);
            if (pathFile == null) {
                continue;
            }
            PsiDirectory directory = PsiManager.getInstance(project).findDirectory(pathFile);
            List<PsiClass> classes = getPsiClassRecur(directory);
            if (CollectionUtils.isNotEmpty(classes)) {
                result.addAll(classes);
            }
        }
        return result;
    }

    /**
     * 查询文件
     */
    @Override
    public List<File> searchFile(String searchPath, FileFilter fileFilter) {
        if (StringUtils.isEmpty(searchPath)) {
            return null;
        }
        File curFile = new File(searchPath);
        if (!curFile.exists()) {
            return null;
        }
        List<File> result = new ArrayList<>();
        if (curFile.isDirectory()) {
            File[] files = curFile.listFiles();
            if (files == null || files.length == 0) {
                return null;
            }
            for (File file : files) {
                List<File> fileList = searchFile(file.getAbsolutePath(), fileFilter);
                if (CollectionUtils.isNotEmpty(fileList)) {
                    result.addAll(fileList);
                }
            }
        } else if (fileFilter != null) {
            if (fileFilter.accept(curFile)) {
                result.add(curFile);
            }
        } else {
            result.add(curFile);
        }
        return result;
    }

    /**
     * 递归地查询所有的接口的psiClass
     *
     * @param directory 目标目录
     */
    private List<PsiClass> getPsiClassRecur(PsiDirectory directory) throws ClassNotFoundException {
        if (directory == null) {
            return null;
        }
        List<PsiClass> result = new ArrayList<>();
        PsiElement[] children = directory.getChildren();
        Project defaultProject = ProjectManager.getInstance().getOpenProjects()[0];

        for (PsiElement e : children) {
            if (e instanceof PsiJavaFileImpl) {
                PsiClass psiClass = ((PsiJavaFile) e).getClasses()[0];
                if (psiClass != null) {
                    PsiClass target = Utils.getClass(psiClass.getQualifiedName(), defaultProject);
                    if (interfaceIndicator.isInterface(target)) {
                        result.add(target);
                    }
                }
            } else if (e instanceof PsiDirectory) {
                List<PsiClass> subClass = getPsiClassRecur((PsiDirectory) e);
                if (CollectionUtils.isNotEmpty(subClass)) {
                    result.addAll(subClass);
                }
            }

        }
        return result;
    }


    @Override
    public void writerFile(String filename, String content) {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new RuntimeException("无法创建目录：" + file.getParentFile().getAbsolutePath());
            }
        }
        try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
