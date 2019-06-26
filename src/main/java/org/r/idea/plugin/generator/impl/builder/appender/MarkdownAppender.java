package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.FileUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @Author Casper
 * @DATE 2019/6/25 21:54
 **/
public class MarkdownAppender implements JarFileAppender {
    @Override
    public void copyFileToJar(JarOutputStream out) {

        String markdownPath = ConfigHolder.getConfig().getMarkdownPath();
        /*获取markdown文件路径信息*/
        List<File> markdowns = ConfigHolder.getConfig().getFileProbe()
                .searchFile(markdownPath, pathname -> pathname.getName().endsWith(".md"));
        if (CollectionUtils.isEmpty(markdowns)) return;
        InputStream in = null;
        try {
            for (File file : markdowns) {
                JarEntry entry = new JarEntry(Constants.JAR_MARKDOWN_PATH + file.getName());
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
