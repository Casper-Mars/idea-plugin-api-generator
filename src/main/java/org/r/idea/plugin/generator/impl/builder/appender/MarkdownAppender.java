package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.FileUtils;

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

    //static {
    //    AppenderChain.addNode(new MarkdownAppender());
    //}

    private String markdownPath;

    public MarkdownAppender(String markdownPath) {
        this.markdownPath = markdownPath;
    }

    @Override
    public void copyFileToJar(JarOutputStream out, Probe probe, String workSpace) {

        /*获取markdown文件路径信息*/
        List<File> markdowns = probe
                .searchFile(markdownPath, pathname -> pathname.getName().endsWith(".md"));
        if (CollectionUtils.isEmpty(markdowns)) {
            return;
        }
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
