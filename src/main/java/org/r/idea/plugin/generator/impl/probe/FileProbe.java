package org.r.idea.plugin.generator.impl.probe;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.local.CoreLocalFileSystem;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.indicators.InterfaceIndicator;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入xml内容，并指定文件名
     *
     * @param filename 文件名
     * @param document xml内容
     */
    @Override
    public void writerFile(String filename, Document document) {
        File file = getFile(filename);
        XMLOutputter xmlOutputter = new XMLOutputter();
        try (OutputStream out = new FileOutputStream(file)) {
            xmlOutputter.output(document, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getFile(String filename) {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new RuntimeException("无法创建目录：" + file.getParentFile().getAbsolutePath());
            }
        }
        return file;
    }

}
