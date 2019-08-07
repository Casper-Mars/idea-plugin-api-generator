package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;


public class XmlAppender implements JarFileAppender {


    @Override
    public void copyFileToJar(JarOutputStream out, Probe probe, String workSpace) {


        File xmlDir = new File(workSpace + Constants.TMP_XML_DIR);
        if (!xmlDir.exists() || !xmlDir.isDirectory()) {
            throw new RuntimeException("xml文件目录不存在");
        }
        /*读取xml目录下的所有xml文件*/
        File[] files = xmlDir.listFiles();

        if (files == null || files.length == 0 || files[0] == null) {
            return;
        }
        try {
            for (File file : files) {
                InputStream in = null;
                JarEntry entry = new JarEntry(Constants.JAR_XML_PATH + file.getName());
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
