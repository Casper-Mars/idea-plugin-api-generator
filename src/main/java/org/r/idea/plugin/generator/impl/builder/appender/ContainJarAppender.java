package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.FileUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @Author Casper
 * @DATE 2019/6/25 22:11
 **/
public class ContainJarAppender implements JarFileAppender {
    @Override
    public void copyFileToJar(JarOutputStream out) {
        Config config = ConfigHolder.getConfig();
        String workSpace = config.getWorkSpace();
        try (JarFile container = new JarFile(workSpace + Constants.COPYOFCONTARINERJAR)) {

            Enumeration<JarEntry> entries = container.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                out.putNextEntry(entry);
                FileUtils.copy(out, container.getInputStream(entry));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
