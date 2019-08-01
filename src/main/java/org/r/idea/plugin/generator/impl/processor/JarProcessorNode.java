package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.builder.JarBuilderImpl;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.io.File;
import java.util.List;

public class JarProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "generating";


    /**
     * 具体节点的处理过程
     *
     * @param context 上下文
     * @return
     */
    @Override
    public boolean process(Context context) {
        context.setTitle(title);
        ConfigBean configurations = context.getConfigurations();
        if (configurations == null) {
            return false;
        }
        String workSpace = configurations.getWorkSpace();
        String srcDir = context.getSrcDir();
        Probe fileProbe = context.getFileProbe();
        if (StringUtils.isEmpty(workSpace) || StringUtils.isEmpty(srcDir)) {
            throw new RuntimeException("缺少输出路径");
        }

        /*查询所有的源文件*/
        List<File> fileList = fileProbe
                .searchFile(srcDir, pathname -> pathname.getName().endsWith(".java"));
        JarBuilder jarBuilder = new JarBuilderImpl();
        jarBuilder.buildJar(fileList, workSpace);
        context.setTargetJarFile(workSpace + Constants.TARGET_JAR_FILE);
        context.updateProgress(0.1f);
        return true;
    }

}
