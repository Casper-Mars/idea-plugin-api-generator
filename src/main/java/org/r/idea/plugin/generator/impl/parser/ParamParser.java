package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocParamRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.indicators.GenericityIndicator;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

/**
 * @ClassName ParamParser
 * @Author Casper
 * @DATE 2019/6/21 11:20
 **/
public class ParamParser {

    private GenericityIndicator genericityIndicator = GenericityIndicator.getInstance();


    public List<Node> parse(PsiMethod method) throws ClassNotFoundException {
        PojoParser pojoParser = new PojoParser();
        Map<String, String> param = getParam(method);
        boolean priority = !CollectionUtils.isEmpty(param);
        PsiParameter[] parameters = method.getParameterList().getParameters();
        List<Node> paramNodeList = new ArrayList<>();
        /*判断参数如下性质：
         * 是否为数组
         * 是否为json实体
         * 是否为实体
         * 是否为泛型
         * 是否必传的
         * 参数的类型
         * 参数名称
         * */
        /*参数过滤
        * 1 过滤数组，list
        * 2 过滤泛型参数
        * 3
        *  */
        for (PsiParameter parameter : parameters) {
            ParamNode paramNode = new ParamNode();
            String rawType = getType(parameter);
            String type = isArray(rawType);
            paramNode.setArray(type.length() < rawType.length());
            paramNode.setJson(
                    Utils.isContainAnnotation("org.springframework.web.bind.annotation.RequestBody", parameter.getAnnotations()));
            paramNode.setEntity(isEntity(parameter));
            paramNode.setGenericity(isGenericity(parameter));
            paramNode.setRequired(isRequery(parameter));
            paramNode.setTypeQualifiedName(getType(parameter));
            paramNode.setName(parameter.getName());
            if (priority) {
                String desc = param.get(parameter.getName());
                if (desc != null) {
                    paramNode.setDesc(desc);
                    paramNodeList.add(paramNode);
                }
            } else {
                paramNode.setDesc("");
                paramNodeList.add(paramNode);
            }
            List<String> entity = getEntity(parameter.getTypeElement());

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

    private String isArray(String type) {
        if (type.contains(Constants.ARRAYFLAG)) {
            type = type.replace(Constants.ARRAYFLAG, "");
        } else if (type.contains(Constants.LISTFLAG)) {
            int start = type.indexOf(Constants.LISTFLAG);
            int end = type.lastIndexOf('>');
            type = type.substring(start + Constants.LISTFLAG.length(), end);
        }
        return type;
    }

    private String getType(PsiParameter parameter) {
        return parameter.getType().getCanonicalText();
    }

    private boolean isEntity(PsiParameter parameter) {
        String canonicalText = parameter.getType().getCanonicalText();


        return false;
    }

    private boolean isGenericity(PsiParameter parameter) {
        String canonicalText = parameter.getType().getCanonicalText();

        return genericityIndicator.isGenricityType(canonicalText, new ArrayList<>());
    }

    private boolean isRequery(PsiParameter parameter) {
        return false;
    }

    private List<String> getEntity(PsiTypeElement element) {
        PsiJavaCodeReferenceElement referenceElement = element.getInnermostComponentReferenceElement();
        if (referenceElement == null) {
            return null;
        }
        List<String> target = new ArrayList<>();
        String name = referenceElement.getQualifiedName();
        if (!Utils.isBaseClass(name)) {
            target.add(name);
        }
        PsiReferenceParameterList parameterList = referenceElement.getParameterList();
        if (parameterList != null) {
            PsiTypeElement[] elements = parameterList.getTypeParameterElements();
            for (PsiTypeElement tmp : elements) {
                List<String> entity = getEntity(tmp);
                if (CollectionUtils.isNotEmpty(entity)) {
                    target.addAll(entity);
                }
            }
        }
        return target;
    }


}
