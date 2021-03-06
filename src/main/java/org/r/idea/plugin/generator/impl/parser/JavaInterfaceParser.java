package org.r.idea.plugin.generator.impl.parser;

import com.intellij.psi.*;
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
 * @ClassName JavaInterfaceParser
 * @Author Casper
 * @DATE 2019/6/21 17:09
 **/
public class JavaInterfaceParser implements Parser {

    @Override
    public Node parse(PsiElement target) throws ClassNotFoundException {
        if (target == null) {
            throw new ClassNotFoundException("需要parse的对象不能为空");
        }
        if (!(target instanceof PsiClass)) {
            throw new ClassNotFoundException("需要parse的对象不是java class");
        }
        PsiClass target0 = (PsiClass) target;
        System.out.println(target0.getQualifiedName());
        InterfaceNode interfaceNode = new InterfaceNode();
        /*设置描述*/
        interfaceNode.setDesc(Utils.getDocCommentDesc(target0.getDocComment()));
        interfaceNode.setName(target0.getName());
        interfaceNode.setBaseUrl(getBaseUrl(target0));
        /*处理方法*/
        List<Node> methods = new ArrayList<>();
        MethodParser methodParser = new MethodParser();
        try {
            for (PsiMethod method : target0.getMethods()) {
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
            e.setMsg(target0.getQualifiedName() + "-" + e.getMsg());
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
