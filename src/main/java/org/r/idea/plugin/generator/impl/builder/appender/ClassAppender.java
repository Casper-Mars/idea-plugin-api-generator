package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.FileUtils;

import java.io.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @Author Casper
 * @DATE 2019/6/25 21:53
 **/
public class ClassAppender implements JarFileAppender {

    @Override
    public void copyFileToJar(JarOutputStream out) {
        String workSpace = ConfigHolder.getConfig().getWorkSpace();
        String classOutputPath = workSpace + Constants.TMP_CLASS_DIR;
        /*获取class文件路径信息*/
        List<File> clazz = ConfigHolder.getConfig().getFileProbe()
                .searchFile(classOutputPath, pathname -> pathname.getName().endsWith(".class"));
        if (CollectionUtils.isEmpty(clazz)) return;
        InputStream in = null;
        try {
            for (File file : clazz) {
                JarEntry entry = new JarEntry(Constants.JAR_FILE_PATH + file.getName());
                out.putNextEntry(entry);
                in = new FileInputStream(file);
                FileUtils.copy(out, in);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
