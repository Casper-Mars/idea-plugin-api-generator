package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.javadoc.PsiDocParamRef;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ParamParser
 * @Author Casper
 * @DATE 2019/6/21 11:20
 **/
public class ParamParser {


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
            paramNode.setJson(
                    Utils.isContainAnnotation("org.springframework.web.bind.annotation.RequestBody", parameter.getAnnotations()));
            /*修饰参数节点,添加参数的属性信息*/
            ObjectParser.decorate(paramNode);
            /*是否必传的*/
            requeryFilter(paramNode);
            /*是否为json实体*/
            paramNode.setName(parameter.getName());
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

    private void requeryFilter(ParamNode paramNode) {
        paramNode.setRequired(false);
    }

}
