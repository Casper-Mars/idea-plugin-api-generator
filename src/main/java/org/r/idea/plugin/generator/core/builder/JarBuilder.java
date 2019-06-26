package org.r.idea.plugin.generator.core.builder;

import com.intellij.util.io.Compressor.Jar;
import org.r.idea.plugin.generator.core.config.ServerManager;

/**
 * @ClassName JarBuilderImpl
 * @Author Casper
 * @DATE 2019/6/24 10:56
 **/

public interface JarBuilder {

    static JarBuilder getInstance() {
        return ServerManager.getServer(JarBuilder.class);
    }

    void buildJar(String srcDir, String workSpace);


}
