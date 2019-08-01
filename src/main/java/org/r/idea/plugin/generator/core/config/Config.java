package org.r.idea.plugin.generator.core.config;

import java.util.List;

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

}
