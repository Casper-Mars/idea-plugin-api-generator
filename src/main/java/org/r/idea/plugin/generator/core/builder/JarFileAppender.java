package org.r.idea.plugin.generator.core.builder;

import java.util.jar.JarOutputStream;

/**
 * @Author Casper
 * @DATE 2019/6/25 21:50
 **/
public interface JarFileAppender {



    void copyFileToJar(JarOutputStream out);


}
