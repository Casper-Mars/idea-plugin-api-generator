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
import java.util.stream.Collectors;

public class ObjectParser {


    public static void decorate(ParamNode paramNode, List<String> typeParamList) throws ClassNotFoundException {

        arrayFilter(paramNode);
        genericityFilter(paramNode);
        entityFilter(paramNode);
        boolean entity = paramNode.isEntity();
        if (!entity) {
            return;
        }
        if (CollectionUtils.isNotEmpty(typeParamList) && typeParamList.contains(paramNode.getTypeQualifiedName())) {
            return;
        }

        ParamNode node = PojoParser.parse(paramNode.getTypeQualifiedName());

        List<Node> children = getChildren(node, paramNode);
        paramNode.setChildren(children);
    }


    private static List<Node> getChildren(ParamNode clazz, ParamNode object) {
        List<Node> children = clazz.getChildren();
        List<Node> targetList = new ArrayList<>();
        List<ParamNode> typeArgList = clazz.getGenericityList();
        List<String> typeArgStrList = typeArgList.stream().map(ParamNode::getTypeQualifiedName).collect(Collectors.toList());
        List<ParamNode> realArgList = object.getGenericityList();
        boolean hasRealArg = CollectionUtils.isNotEmpty(realArgList);
        if (CollectionUtils.isEmpty(children)) return targetList;
        for (Node node : children) {
            ParamNode paramNode = ((ParamNode) node).clone();
            int i = -1;
            if (hasRealArg && (i = typeArgStrList.indexOf(paramNode.getTypeQualifiedName())) != -1) {
                paramNode.setTypeQualifiedName(Utils.getType(realArgList.get(i)));
            }
            boolean genericity = paramNode.isGenericity();
            if (genericity) {
                /*形参*/
                List<ParamNode> childParamList = paramNode.getGenericityList();
                List<ParamNode> realParamList = new ArrayList<>();
                for (ParamNode s : childParamList) {
                    if (hasRealArg && (i = typeArgList.indexOf(s)) != -1) {
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
            List<String> stringArrayList = new ArrayList<>(Arrays.asList(split));
            List<ParamNode> target = new ArrayList<>();
            for (String s : stringArrayList) {
                ParamNode parse;
                try {
                    parse = PojoParser.parse(s);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    parse = new ParamNode();
                    parse.setTypeQualifiedName(s);
                }
                target.add(parse);
            }
            paramNode.setGenericityList(target);
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
