package org.r.idea.plugin.generator.core.builder;

import java.util.List;

import org.jdom2.Document;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName DocBuilder
 * @Author Casper
 * @DATE 2019/6/21 17:02
 **/

public interface DocBuilder {

    static DocBuilder getInstance() {
        return ServerManager.getServer(DocBuilder.class);
    }


    Document buildDoc(List<Node> nodes);

}
