package org.r.idea.plugin.generator.core.probe;

import com.intellij.psi.PsiClass;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.intellij.psi.PsiElement;
import org.jdom2.Document;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;

/**
 * @ClassName Probe
 * @Author Casper
 * @DATE 2019/6/21 16:54
 **/

public interface Probe {

    static Probe getInstance() {
        return ServerManager.getServer(Probe.class);
    }

    List<PsiElement> getAllInterfaceClass(List<String> interfaceFilePath) throws ClassNotFoundException;

    /**
     * 字符串写入文件，并指定文件名
     *
     * @param filename 文件名
     * @param content  内容字符串
     */
    void writerFile(String filename, String content);

    /**
     * 写入xml内容，并指定文件名
     *
     * @param filename 文件名
     * @param document xml内容
     */
    void writerFile(String filename, Document document);

    List<File> searchFile(String searchPath, FileFilter fileFilter);


}
