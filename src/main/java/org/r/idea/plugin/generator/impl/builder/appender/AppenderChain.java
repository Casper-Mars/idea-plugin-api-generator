package org.r.idea.plugin.generator.impl.builder.appender;

import org.r.idea.plugin.generator.core.builder.JarFileAppender;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/6/25 22:01
 **/
public class AppenderChain {

    private List<JarFileAppender> chain = new ArrayList<>();

    public static List<JarFileAppender> getAppenderChain() {
        AppenderChain appenderChain = new AppenderChain();
//        appenderChain
//                .addAppender(new ClassAppender())
//                .addAppender(new MarkdownAppender())
//                .addAppender(new ContainJarAppender());
        return appenderChain.getChain();
    }

    public AppenderChain addAppender(JarFileAppender appender) {
        this.chain.add(appender);
        return this;
    }

    public List<JarFileAppender> getChain() {
        return this.chain;
    }


}
