package org.r.idea.plugin.generator.impl.parser;

import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectParser {


    public static ParamNode decorate(ParamNode paramNode) throws ClassNotFoundException {

        PojoParser pojoParser = new PojoParser();
        ParamNode node = pojoParser.parse(paramNode.getTypeQualifiedName());

        List<String> genericityList = paramNode.getGenericityList();
        if (CollectionUtils.isNotEmpty(genericityList)) {
            List<String> list = node.getGenericityList();
        }


        return paramNode;
    }


    private List<Node> getChildren(ParamNode clazz, ParamNode object) {


        if (clazz.isGenericity()) {

            List<String> typeArgList = clazz.getGenericityList();
            List<String> realArgList = object.getGenericityList();
            List<Node> children = clazz.getChildren();
            List<Node> targetList = new ArrayList<>(Arrays.asList(children.toArray(new Node[0]).clone()));

            for (Node node : targetList) {
                ParamNode paramNode = (ParamNode) node;
                int i = -1;
                if ((i = typeArgList.indexOf(paramNode.getTypeQualifiedName())) != -1) {
                    paramNode.setTypeQualifiedName(realArgList.get(i));
                }
                if (paramNode.isGenericity()) {
                    List<String> childParamList = paramNode.getGenericityList();


                }


            }


        }


        return null;

    }


}
