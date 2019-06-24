package org.r.idea.plugin.generator.impl.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.r.idea.plugin.generator.core.ConfigHolder;
import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.FileUtils;

/**
 * @Author Casper
 * @DATE 2019/6/23 21:09
 **/
public class JarBuilderImpl implements JarBuilder {

    /**
     * 容器jar在本jar包中的位置
     */
    private String contarinerJar = "/container.jar";

    private String copyOfContarinerJar = "lib/container.jar";
    /**
     * 依赖jar在本jar包中的位置
     */
    private String dependenciesJar = "/dependencies.jar";

    private String copyOfDependenciesJar = "lib/dependencies.jar";

    /**
     * 目标可运行jar包
     */
    private String productJar = "api-doc.jar";


    @Override
    public void buildJar(String srcDir, String workSpace) {

        /*查询所有的源文件*/
        List<File> fileList = ConfigHolder.getConfig().getFileProbe()
            .searchFile(srcDir, pathname -> pathname.getName().endsWith(".java"));
        List<String> srcJava = fileList.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(srcJava)) {
            System.out.println("源文件不存在");
            return;
        }
        /*复制编译环境*/
        copyDependences(workSpace);
        /*复制容器*/
        copyContainer(workSpace);
        /*编译源文件,并储存为临时文件*/
        List<File> clazz = compile(srcJava, workSpace);
        /*copy容器*/
        generateContainer(clazz, workSpace);




    }

    /**
     * 复制编译依赖到工作空间中
     */
    private void copyDependences(String workSpace) {
        File dependence = new File(workSpace + copyOfDependenciesJar);

        try (
            InputStream in = this.getClass().getResourceAsStream(dependenciesJar);
            OutputStream out = new FileOutputStream(dependence);
        ) {
            FileUtils.copy(out, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制容器到工作空间中
     */
    private void copyContainer(String workSpace) {
        File dependence = new File(workSpace + copyOfContarinerJar);

        try (
            InputStream in = this.getClass().getResourceAsStream(contarinerJar);
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
     * @param javaSrc 源文件路径信息
     * @param workSpace 工作空间
     */
    private List<File> compile(List<String> javaSrc, String workSpace) {

        String classOutputPath = workSpace + Constants.TMP_CLASS_DIR;
        /*判读目录是否存在*/
        File clazzsDir = new File(classOutputPath);
        if (!clazzsDir.exists()) {
            if (!clazzsDir.mkdirs()) {
                throw new RuntimeException("无法创建目录：" + classOutputPath);
            }
        }
        /*构建编译参数*/
        int ext = 6;
        String[] filenames = new String[javaSrc.size() + ext];
        int i = 0;

        filenames[i++] = "-classpath";
        filenames[i++] = workSpace + copyOfDependenciesJar;
        filenames[i++] = "-d";
        filenames[i++] = classOutputPath;
        filenames[i++] = "-encoding";
        filenames[i++] = "UTF-8";
        for (String path : javaSrc) {
            filenames[i++] = path;
        }
        /*获取编译器*/
        JavaCompiler javac = getJavaCompiler();
        if (javac == null) {
            throw new RuntimeException("无法获取编译器");
        }
        javac.run(null, null, null, filenames);
        /*获取class文件路径信息*/
        List<File> clazz = ConfigHolder.getConfig().getFileProbe()
            .searchFile(classOutputPath, pathname -> pathname.getName().endsWith(".class"));
        if (CollectionUtils.isEmpty(clazz)) {
            throw new RuntimeException("编译失败");
        }
        return clazz;
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
     * 生产容器
     */
    private void generateContainer(List<File> clazzs, String workSpace) {
        /*获取容器*/
        File targetJar = new File(workSpace + productJar);
        InputStream in = null;
        try (
            JarOutputStream out = new JarOutputStream(new FileOutputStream(targetJar));
        ) {
            JarFile container = new JarFile(workSpace + copyOfContarinerJar);
            Enumeration<JarEntry> entries = container.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                out.putNextEntry(entry);
                FileUtils.copy(out, container.getInputStream(entry));
            }
            container.close();
            for (File file : clazzs) {
                JarEntry entry = new JarEntry(Constants.JAR_FILE_PATH + file.getName());
                out.putNextEntry(entry);
                in = new FileInputStream(file);
                FileUtils.copy(out, in);
                in.close();
            }
            out.finish();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
