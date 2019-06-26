package org.r.idea.plugin.generator.core;

import com.intellij.psi.PsiClass;
import java.util.ArrayList;
import java.util.List;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName Generator
 * @Author Casper
 * @DATE 2019/6/21 16:51
 **/
public class Generator {


    private Config config;

    public Generator(Config config) {
        this.config = config;
        ConfigHolder.setConfig(config);
    }

    public void doGenerate() {
        //List<PsiClass> interfaceClass = config.getFileProbe().getAllInterfaceClass(config.getInterfaceFilesPath());
        //System.out.println("总共找到" + interfaceClass.size() + "个接口：");
        //for (PsiClass target : interfaceClass) {
        //    System.out.println(target.getQualifiedName());
        //}
        //List<Node> interfaceNode = new ArrayList<>();
        //for (PsiClass target : interfaceClass) {
        //    try {
        //        long s = System.currentTimeMillis();
        //        Node parse = config.getInterfaceParser().parse(target);
        //        interfaceNode.add(parse);
        //        System.out.println(
        //            "接口：" + target.getQualifiedName() + " 转发完成，耗时：" + (System.currentTimeMillis() - s) + " ms");
        //    } catch (ClassNotFoundException e) {
        //        e.printStackTrace();
        //    }
        //}
        //System.out.println("完成");
        //List<FileBO> docList = config.getDocBuilder().buildDoc(interfaceNode);
        //
        //String srcDir = config.getFileProbe().saveDoc(docList, config.getWorkSpace());
        //
        //config.getJarBuilder().buildJar(srcDir, config.getWorkSpace());

    }


}
