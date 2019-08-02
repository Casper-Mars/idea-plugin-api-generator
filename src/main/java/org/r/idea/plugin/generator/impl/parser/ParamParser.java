package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocParamRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;
import org.apache.batik.css.engine.value.svg12.ICCNamedColor;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.*;

/**
 * @ClassName ParamParser
 * @Author Casper
 * @DATE 2019/6/21 11:20
 **/
public class ParamParser {


    private PojoParser pojoParser = new PojoParser();

    public List<Node> parse(PsiMethod method) throws ClassNotFoundException {
        Map<String, String> param = getParam(method);
        boolean priority = !CollectionUtils.isEmpty(param);
        PsiParameter[] parameters = method.getParameterList().getParameters();
        List<Node> paramNodeList = new ArrayList<>();
        for (PsiParameter parameter : parameters) {
            ParamNode paramNode = new ParamNode();
            if (priority) {
                String desc = param.get(parameter.getName());
                if (desc != null) {
                    paramNode.setDesc(desc);
                    paramNodeList.add(paramNode);
                } else {
                    continue;
                }
            } else {
                paramNode.setDesc("");
                paramNodeList.add(paramNode);
            }
            paramNode.setTypeQualifiedName(getType(parameter));
            /*过滤数组、list,是否为数组*/
            arrayFilter(paramNode);
            /*过滤泛型参数,是否为泛型*/
            genericityFilter(paramNode);
            /*是否为实体*/
            entityFilter(paramNode);
            /*是否必传的*/
            requeryFilter(paramNode);
            /*是否为json实体*/
            paramNode.setJson(
                    Utils.isContainAnnotation("org.springframework.web.bind.annotation.RequestBody", parameter.getAnnotations()));
            paramNode.setName(parameter.getName());
            getChildren(paramNode);
        }
        return paramNodeList;
    }

    /**
     * 获取方法的参数
     *
     * @param method 方法
     * @return
     */
    private Map<String, String> getParam(PsiMethod method) {
        PsiDocComment docComment = method.getDocComment();

        if (docComment == null) {
            return null;
        }
        PsiDocTag[] tags = docComment.getTags();
        if (tags.length == 0) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        String paramName = "";
        StringBuilder paramDesc = new StringBuilder();
        for (PsiDocTag tag : tags) {
            for (PsiElement data : tag.getDataElements()) {
                if (data instanceof PsiDocParamRef) {
                    paramName = data.getFirstChild().getText();
                }
                if (data instanceof PsiDocToken) {
                    paramDesc.append(data.getText());
                }
            }
            result.put(paramName, paramDesc.toString());
            paramName = "";
            paramDesc.setLength(0);
        }
        return result;
    }

    private String getType(PsiParameter parameter) {
        return parameter.getType().getCanonicalText();
    }

    private void entityFilter(ParamNode paramNode) {
        paramNode.setEntity(!Utils.isBaseClass(paramNode.getTypeQualifiedName()));
    }

    private void arrayFilter(ParamNode paramNode) {
        String type = paramNode.getTypeQualifiedName();
        String newType = Utils.isArrayType(type);
        paramNode.setTypeQualifiedName(newType);
        paramNode.setArray(newType.length() < type.length());

    }

    private void genericityFilter(ParamNode paramNode) {
        String type = paramNode.getTypeQualifiedName();
        int left = type.indexOf('<');
        int right = type.lastIndexOf('>');
        if (left - right != 0) {
            String substring = type.substring(left + 1, right);
            String[] split = substring.split(Constants.SPLITOR);
            paramNode.setTypeQualifiedName(type.substring(0, left));
            paramNode.setGenericityList(new ArrayList<>(Arrays.asList(split)));
        }
    }

    private void requeryFilter(ParamNode paramNode) {
        paramNode.setRequired(false);
    }

    private void getChildren(ParamNode paramNode) throws ClassNotFoundException {

        ParamNode parse = pojoParser.parse(paramNode.getTypeQualifiedName());
        List<Node> children = parse.getChildren();
        List<String> realParamList = paramNode.getGenericityList();
        /*建立泛型参数的index*/
        Map<String, Integer> index = new HashMap<>();
        if (parse.isGenericity()) {
            for (int i = 0; i < parse.getGenericityList().size(); i++) {
                index.put(parse.getGenericityList().get(i), i);
            }
        }

        List<Node> target = new ArrayList<>();
        for (Node child : children) {
            ParamNode tmp = ((ParamNode) child).clone();
            if (parse.getGenericityList().contains(tmp.getTypeQualifiedName())) {
                Integer i = index.get(tmp.getTypeQualifiedName());
                if (i != null && CollectionUtils.isNotEmpty(realParamList)) {
                    String s = realParamList.get(i);
                    tmp.setTypeQualifiedName(s);
                }
            }
            if (tmp.isGenericity()) {
                List<String> tmpList = new ArrayList<>();
                for (String s : tmp.getGenericityList()) {
                    Integer i = index.get(s);
                    if (i != null && CollectionUtils.isNotEmpty(realParamList)) {
                        tmpList.add(realParamList.get(i));
                    } else {
                        tmpList.add(s);
                    }
                }
                tmp.setGenericityList(tmpList);
                getChildren(tmp);
            }
            target.add(tmp);
        }
        paramNode.setChildren(target);
    }


}
