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
        /*获取子属性*/
        ParamNode prototype = EntityContainer.getEntity(paramNode.getTypeQualifiedName());
        if (prototype == null) {
            prototype = PojoParser.parseEntity(paramNode.getTypeQualifiedName());
            EntityContainer.addEntity(paramNode.getTypeQualifiedName(), prototype);
        }
        initChildren(paramNode, prototype);

    }


    private static List<Node> initChildren(ParamNode paramNode, ParamNode prototype) {
        List<Node> children = prototype.getChildren();
        List<Node> targetList = new ArrayList<>();

        if (CollectionUtils.isEmpty(children)) {
            return new ArrayList<>();
        }
        List<String> paramRealList = paramNode.getGenericityList();
        List<String> paramTypeList = prototype.getGenericityList();

        if (CollectionUtils.isNotEmpty(paramTypeList) && CollectionUtils.isEmpty(paramRealList)) {
            paramTypeList.forEach(t -> paramRealList.add("Object"));
        }

        for (Node node : children) {
            int i = -1;
            ParamNode childNode = ((ParamNode) node).clone();
            arrayFilter(childNode);
            boolean array = childNode.isArray();
            genericityFilter(childNode);
            if (childNode.isGenericity()) {
                List<String> tmpList = childNode.getGenericityList();
                for (int j = 0; j < tmpList.size(); j++) {
                    if ((i = paramTypeList.indexOf(tmpList.get(j))) != -1) {
                        tmpList.set(j, paramRealList.get(i));
                    }
                }
            } else if ((i = paramTypeList.indexOf(childNode.getTypeQualifiedName())) != -1) {
                childNode.setTypeQualifiedName(paramRealList.get(i));
            }
            try {
                decorate(childNode);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            childNode.setArray(array);
            targetList.add(childNode);
        }
        paramNode.setChildren(targetList);
        return targetList;
    }


    private static void arrayFilter(ParamNode paramNode) {
        String type = paramNode.getTypeQualifiedName();
        String newType = Utils.isArrayType(type);
        paramNode.setTypeQualifiedName(newType);
        paramNode.setArray(newType.length() < type.length());
    }

    private static void genericityFilter(ParamNode paramNode) {
        if (CollectionUtils.isNotEmpty(paramNode.getGenericityList())) {
            return;
        }
        String type = paramNode.getTypeQualifiedName();
        int left = type.indexOf('<');
        int right = type.lastIndexOf('>');
        if (left - right != 0) {
            String substring = type.substring(left + 1, right);
            String[] split = substring.split(Constants.SPLITOR);
            paramNode.setTypeQualifiedName(type.substring(0, left));
            List<String> stringArrayList = new ArrayList<>(Arrays.asList(split));
            paramNode.setGenericityList(stringArrayList);
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
