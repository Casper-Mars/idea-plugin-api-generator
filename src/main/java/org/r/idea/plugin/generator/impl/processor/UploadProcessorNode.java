package org.r.idea.plugin.generator.impl.processor;

import org.r.idea.plugin.generator.core.config.ConfigBean;
import org.r.idea.plugin.generator.core.config.SSHConfigBean;
import org.r.idea.plugin.generator.core.processor.AbstractProcessorNode;
import org.r.idea.plugin.generator.core.upload.Deliveryman;
import org.r.idea.plugin.generator.impl.upload.DeliverymanImpl;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @author casper
 */
public class UploadProcessorNode extends AbstractProcessorNode<Context> {

    private String title = "uploading";

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
        SSHConfigBean sshConfigBean = configurations.getSshConfigBean();
        String targetFile = context.getTargetJarFile();
        if (sshConfigBean == null || StringUtils.isEmpty(targetFile)) {
            return false;
        }
        Deliveryman deliveryman = new DeliverymanImpl(targetFile,
                sshConfigBean.getHost(),
                sshConfigBean.getUsername(),
                sshConfigBean.getPassword(),
                sshConfigBean.getPort(),
                sshConfigBean.getRemotePath());
        deliveryman.doDeliver();
        context.updateProgress(0.1f);
        return true;
    }
}
