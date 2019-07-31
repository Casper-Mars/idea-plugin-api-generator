package org.r.idea.plugin.generator.impl.processor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Casper
 * @DATE 2019/7/31 21:23
 **/
public class SearchProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "search file";


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
        List<String> interfaceFilePaths = configurations.getInterfaceFilePaths();
        if (CollectionUtils.isEmpty(interfaceFilePaths)) {
            return false;
        }
        List<PsiClass> allInterfaceClass = new ArrayList<>();
        Probe fileProbe = new FileProbe();
        StringBuilder sb = new StringBuilder();
        ApplicationManager.getApplication().runReadAction(() -> {
            try {
                allInterfaceClass.addAll(fileProbe.getAllInterfaceClass(interfaceFilePaths));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                sb.append(e.getMsg());
            }
        });
        if (sb.length() != 0) {
            throw new RuntimeException(sb.toString());
        }
        context.setInterfaceClass(allInterfaceClass);
        context.updateProgress(0.1f);
        return true;
    }


}
