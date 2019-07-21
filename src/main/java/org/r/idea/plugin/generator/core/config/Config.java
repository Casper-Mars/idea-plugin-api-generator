package org.r.idea.plugin.generator.core.config;

import java.io.File;
import java.util.List;

import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.core.probe.Probe;

/**
 * @ClassName Config
 * @Author Casper
 * @DATE 2019/6/21 16:52
 **/

public interface Config {


    List<String> getInterfaceFilesPath();

    String getWorkSpace();

    String getMarkdownPath();

    boolean isDebug();

    String getUsername();

    String getPassword();

    String getHost();

    String getRemotePath();


}
