package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName ResponeParser
 * @Author Casper
 * @DATE 2019/6/22 10:31
 **/
public class ResponeParser {


    public Node parse(PsiMethod method) throws ClassNotFoundException {

        /*获取返回类型，如果注释有指定类型，则优先获取注释的，否则直接获取返回类型*/
        String responeType = getResponeType(method);
        ParamNode paramNode = new ParamNode();
        paramNode.setTypeQualifiedName(responeType);
        ObjectParser.decorate(paramNode);
        return paramNode;
    }

    private String getResponeType(PsiMethod method) {

        String type = method.getReturnType().getCanonicalText();

        /*如果有注释且注释中指定了返回类型，则取注释的返回类型*/
        if (method.getDocComment() != null) {
            StringBuilder sb = new StringBuilder();
            for (PsiDocTag tag : method.getDocComment().getTags()) {
                if (tag.getName().equals(Constants.DOCCOMMENT_RETURN)) {
                    for (PsiElement e : tag.getDataElements()) {
                        sb.append(e.getText());
                    }
                    String s = sb.toString().trim();
                    if (StringUtils.isNotEmpty(s)) {
                        type = s;
                    }
                    break;
                }
            }
        }

        return type;
    }

}
