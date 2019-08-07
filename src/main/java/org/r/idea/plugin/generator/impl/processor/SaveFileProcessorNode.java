package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.util.List;

public class SaveFileProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "saving";

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
        List<FileBO> fileBOS = context.getFileBOS();
        if (CollectionUtils.isEmpty(fileBOS) || StringUtils.isEmpty(workSpace)) {
            return false;
        }
        Probe probe = context.getFileProbe();
        String filePrefix = workSpace + Constants.TMP_XML_DIR;
        for (FileBO fileBO : fileBOS) {
            if (StringUtils.isEmpty(fileBO.getPresentName())) {
                // TODO: 2019/6/24 文件名为空时应该记录下来
                continue;
            }
            probe.writerFile(filePrefix + fileBO.getPresentName(), fileBO.getContent());
        }
        context.updateProgress(0.1f);
        return true;
    }
}
