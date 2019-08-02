package org.r.idea.plugin.generator.impl.parser;

import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectParser {


    public static void decorate(ParamNode paramNode) throws ClassNotFoundException {

        arrayFilter(paramNode);
        genericityFilter(paramNode);
        entityFilter(paramNode);
        boolean entity = paramNode.isEntity();
        if (!entity) {
            return;
        }

        PojoParser pojoParser = new PojoParser();
        ParamNode node = pojoParser.parse(paramNode.getTypeQualifiedName());

        List<Node> children = getChildren(node, paramNode);
        paramNode.setChildren(children);
    }


    private static List<Node> getChildren(ParamNode clazz, ParamNode object) {
        List<Node> children = clazz.getChildren();
        List<Node> targetList = new ArrayList<>();
        List<String> typeArgList = clazz.getGenericityList();
        List<String> realArgList = object.getGenericityList();
        if (CollectionUtils.isEmpty(children)) return targetList;
        for (Node node : children) {
            ParamNode paramNode = ((ParamNode) node).clone();
            int i = -1;
            if ((i = typeArgList.indexOf(paramNode.getTypeQualifiedName())) != -1) {
                paramNode.setTypeQualifiedName(realArgList.get(i));
            }
            boolean genericity = paramNode.isGenericity();
            if (genericity) {
                /*形参*/
                List<String> childParamList = paramNode.getGenericityList();
                List<String> realParamList = new ArrayList<>();
                for (String s : childParamList) {
                    if ((i = typeArgList.indexOf(s)) != -1) {
                        realParamList.add(realArgList.get(i));
                    }
                }
                paramNode.setGenericityList(realParamList);
            }
            targetList.add(paramNode);
        }
        return targetList;
    }


    private static void arrayFilter(ParamNode paramNode) {
        String type = paramNode.getTypeQualifiedName();
        String newType = Utils.isArrayType(type);
        paramNode.setTypeQualifiedName(newType);
        paramNode.setArray(newType.length() < type.length());
    }

    private static void genericityFilter(ParamNode paramNode) {
        String type = paramNode.getTypeQualifiedName();
        int left = type.indexOf('<');
        int right = type.lastIndexOf('>');
        if (left - right != 0) {
            String substring = type.substring(left + 1, right);
            String[] split = substring.split(Constants.SPLITOR);
            paramNode.setTypeQualifiedName(type.substring(0, left));
            paramNode.setGenericityList(new ArrayList<>(Arrays.asList(split)));
            paramNode.setGenericity(true);
        } else {
            paramNode.setGenericity(false);
            paramNode.setGenericityList(new ArrayList<>());
        }
    }

    private static void entityFilter(ParamNode paramNode) {
        paramNode.setEntity(!Utils.isBaseClass(paramNode.getTypeQualifiedName()));
    }

}
