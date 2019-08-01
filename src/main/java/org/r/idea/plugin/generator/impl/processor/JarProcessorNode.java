package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.builder.JarFileAppender;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.builder.appender.AppenderChain;
import org.r.idea.plugin.generator.impl.builder.appender.ClassAppender;
import org.r.idea.plugin.generator.impl.builder.appender.ContainJarAppender;
import org.r.idea.plugin.generator.impl.builder.appender.MarkdownAppender;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.FileUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

public class JarProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "generating";

    /**
     * 容器jar在本jar包中的位置
     */
    private String contarinerJar = "/container.jar";

    /**
     * 依赖jar在本jar包中的位置
     */
    private String dependenciesJar = "/dependencies.jar";

    private String copyOfDependenciesJar = "lib/dependencies.jar";

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
        List<String> srcJava = fileList.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(srcJava)) {
            System.out.println("源文件不存在");
            return false;
        }
        /*复制编译环境*/
        copyJar(dependenciesJar, workSpace + copyOfDependenciesJar);
        /*复制容器*/
        copyJar(contarinerJar, workSpace + Constants.COPYOFCONTARINERJAR);
        /*编译源文件,并储存为临时文件*/
        compile(srcJava, workSpace);
        /*获取appender链*/
        List<JarFileAppender> appenderChain = getAppenderChain(context);
        /*copy容器*/
        File targetJar = new File(workSpace + Constants.TARGET_JAR_FILE);
        try (JarOutputStream out = new JarOutputStream(new FileOutputStream(targetJar))) {
            /*复制文件*/
            for (JarFileAppender appender : appenderChain) {
                appender.copyFileToJar(out, fileProbe, workSpace);
            }
            out.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*如果是debug模式，则不处理，否则删除所有临时文件*/
        if (!configurations.isDebug()) {
            /*清除所有的临时文件*/
            eraseTempFile(workSpace);
        }
        context.setTargetJarFile(workSpace + Constants.TARGET_JAR_FILE);
        context.updateProgress(0.1f);
        return true;
    }


    private void copyJar(String src, String target) {
        File dependence = new File(target);
        if (!dependence.getParentFile().exists()) {
            if (!dependence.getParentFile().mkdirs()) {
                throw new RuntimeException("无法创建路径：" + dependence.getAbsolutePath());
            }
        }
        try (
                InputStream in = this.getClass().getResourceAsStream(src);
                OutputStream out = new FileOutputStream(dependence)
        ) {
            FileUtils.copy(out, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 编译源文件并输入到指定的临时目录
     *
     * @param javaSrc   源文件路径信息
     * @param workSpace 工作空间
     */
    private void compile(List<String> javaSrc, String workSpace) {

        String classOutputPath = workSpace + Constants.TMP_CLASS_DIR;
        /*判读目录是否存在*/
        File clazzsDir = new File(classOutputPath);
        if (!clazzsDir.exists()) {
            if (!clazzsDir.mkdirs()) {
                throw new RuntimeException("无法创建目录：" + classOutputPath);
            }
        }
        /*构建编译参数*/
        int ext = 7;
        String[] filenames = new String[javaSrc.size() + ext];
        int i = 0;

        filenames[i++] = "-classpath";
        filenames[i++] = workSpace + copyOfDependenciesJar;
        filenames[i++] = "-d";
        filenames[i++] = classOutputPath;
        filenames[i++] = "-encoding";
        filenames[i++] = "UTF-8";
        filenames[i++] = "-parameters";
        for (String path : javaSrc) {
            filenames[i++] = path;
        }
        /*获取编译器*/
        JavaCompiler javac = getJavaCompiler();
        if (javac == null) {
            throw new RuntimeException("无法获取编译器");
        }
        int result = javac.run(null, null, null, filenames);
        if (result != 0) {
            throw new RuntimeException("编译失败");
        }
    }

    /**
     * 获取java编译器
     */
    private JavaCompiler getJavaCompiler() {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if (javac == null) {
            try {
                Class<?> aClass = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = aClass.getMethod("create");
                javac = (JavaCompiler) create.invoke(null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return javac;
    }

    /**
     * 获取构造链
     *
     * @param context
     * @return
     */
    private List<JarFileAppender> getAppenderChain(Context context) {
        String workSpace = context.getConfigurations().getWorkSpace();
        String markdownPath = context.getConfigurations().getMarkdownPath();
        AppenderChain appenderChain = new AppenderChain();
        appenderChain
                .addAppender(new ClassAppender(workSpace + Constants.TMP_CLASS_DIR))
                .addAppender(new MarkdownAppender(markdownPath))
                .addAppender(new ContainJarAppender());
        return appenderChain.getChain();
    }

    /**
     * 清除所有的临时文件
     *
     * @param workSpace
     */
    private void eraseTempFile(String workSpace) {

        try {
            /*删除java文件夹*/
            FileUtils.deleteDir(new File(workSpace + "java/"));
            /*删除class文件夹*/
            FileUtils.deleteDir(new File(workSpace + "class/"));
            /*删除lib文件夹*/
            FileUtils.deleteDir(new File(workSpace + "lib/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
