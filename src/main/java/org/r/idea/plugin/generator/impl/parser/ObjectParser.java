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
        initSuperClass(paramNode, prototype);
    }


    /**
     * 处理父类
     *
     * @param paramNode
     * @param prototype
     * @throws ClassNotFoundException
     */
    private static void initSuperClass(ParamNode paramNode, ParamNode prototype) throws ClassNotFoundException {
        ParamNode superClass = prototype.getSuperClass();
        if (superClass == null) {
            return;
        }

        List<String> paramRealList = paramNode.getGenericityList();
        List<String> paramTypeList = superClass.getGenericityList();

        if (CollectionUtils.isNotEmpty(paramTypeList) && CollectionUtils.isEmpty(paramRealList)) {
            paramTypeList.forEach(t -> paramRealList.add("Object"));
        }
        int i = -1;
        for (int j = 0; j < paramTypeList.size(); j++) {
            if ((i = paramTypeList.indexOf(paramTypeList.get(j))) != -1) {
                paramTypeList.set(j, paramRealList.get(i));
            }
        }

        decorate(superClass);
        if (CollectionUtils.isNotEmpty(superClass.getChildren())) {
            for (Node child : superClass.getChildren()) {
                ParamNode node = (ParamNode) child;
                if (paramNode.isRequired()) {
                    node.setRequired(paramNode.isRequired());
                    paramNode.getChildren().add(node);
                }
            }
        }
    }


    /**
     * 处理子属性
     *
     * @param paramNode
     * @param prototype
     */
    private static void initChildren(ParamNode paramNode, ParamNode prototype) {
        List<Node> children = prototype.getChildren();
        List<Node> targetList = new ArrayList<>();

        if (CollectionUtils.isEmpty(children)) {
            return;
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
            childNode.setRequired(paramNode.isRequired());
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
