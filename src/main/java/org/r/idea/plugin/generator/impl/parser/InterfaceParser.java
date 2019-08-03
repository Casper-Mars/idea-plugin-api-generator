package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNameValuePair;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.impl.nodes.MethodNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName InterfaceParser
 * @Author Casper
 * @DATE 2019/6/21 17:09
 **/
public class InterfaceParser implements Parser {

    @Override
    public Node parse(PsiClass target) throws ClassNotFoundException {
        if (target == null) {
            throw new ClassNotFoundException("需要parse的对象不能为空");
        }
        InterfaceNode interfaceNode = new InterfaceNode();
        /*设置描述*/
        if (target.getDocComment() != null) {
            interfaceNode.setDesc(Utils.getDocCommentDesc(target.getDocComment().getDescriptionElements()));
        }
        interfaceNode.setName(target.getName());
        interfaceNode.setBaseUrl(getBaseUrl(target));
        /*处理方法*/
        List<Node> methods = new ArrayList<>();
        MethodParser methodParser = new MethodParser();
        try {
            for (PsiMethod method : target.getMethods()) {
                System.out.println("----" + method.getName());
                MethodNode methodNode = methodParser.parse(method);
                if (methodNode == null) {
                    continue;
                }
                methodNode.setUrl(interfaceNode.getBaseUrl() + methodNode.getUrl());
                methods.add(methodNode);
                System.out.println("----" + method.getName() + "-----finish");
            }
        } catch (ClassNotFoundException e) {
            e.setMsg(target.getQualifiedName() + "-" + e.getMsg());
            throw e;
        }
        interfaceNode.setChildren(methods);
        return interfaceNode;
    }


    private String getBaseUrl(PsiClass target) {

        PsiAnnotation[] annotations = target.getAnnotations();

        PsiAnnotation req = Utils
                .findAnnotationByName("org.springframework.web.bind.annotation.RequestMapping", annotations);
        if (req == null || CollectionUtils.isEmpty(req.getAttributes())) {
            return "";
        }

        PsiNameValuePair psiNameValuePair = (PsiNameValuePair) req.getAttributes().get(0);
        return psiNameValuePair.getLiteralValue();
    }


}
