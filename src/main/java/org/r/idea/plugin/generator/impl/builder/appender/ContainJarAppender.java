package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.FileUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;

/**
 * @Author Casper
 * @DATE 2019/6/25 22:11
 **/
public class ContainJarAppender implements JarFileAppender {

    @Override
    public void copyFileToJar(JarOutputStream out, Probe probe, String workSpace) {

        try (JarFile container = new JarFile(workSpace + Constants.COPYOFCONTARINERJAR)) {
            Enumeration<JarEntry> entries = container.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                try {
                    out.putNextEntry(entry);
                    FileUtils.copy(out, container.getInputStream(entry));
                } catch (ZipException ze) {
                    ze.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
