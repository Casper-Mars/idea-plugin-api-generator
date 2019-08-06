package org.r.idea.plugin.generator.impl.builder;

import org.jdom2.Document;
import org.jdom2.Element;
import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.nodes.Node;

import java.util.List;

/**
 * @ClassName DocBuilderImpl
 * @Author Casper
 * @DATE 2019/6/24 11:05
 **/
public class DocBuilderImpl implements DocBuilder {

    private XmlBuilder xmlBuilder = new XmlBuilder();

    @Override
    public Document buildDoc(List<Node> nodes) {
        Element element = xmlBuilder.buildContent(nodes);
        return new Document(element);
    }

}
