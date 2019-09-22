package org.r.idea.plugin.generator.ktimpl.parser;

import com.intellij.psi.PsiElement;
import org.jetbrains.kotlin.psi.*;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.impl.nodes.InterfaceNode;
import org.r.idea.plugin.generator.ktimpl.Utils;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author casper
 */
public class KtInterfaceParser implements Parser {
    /**
     * 根据class对象转化出信息
     *
     * @param target class对象
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Node parse(PsiElement target) throws ClassNotFoundException {

        if (target == null) {
            throw new ClassNotFoundException("需要parse的对象不能为空");
        }
        if (!(target instanceof KtClass)) {
            throw new ClassNotFoundException("需要parse的对象不是kotlin class");
        }

        KtClass target0 = (KtClass)target;
        System.out.println(target0.getFqName());

        InterfaceNode interfaceNode = new InterfaceNode();
        /*设置描述*/
        interfaceNode.setDesc(Utils.getDocCommentDesc(target0.getDocComment()));
        interfaceNode.setName(target0.getName());
        interfaceNode.setBaseUrl(getBaseUrl(target0));
        /*处理方法*/
        List<Node> methods = new ArrayList<>();





        return null;
    }


    private String getBaseUrl(KtClass target) {
        List<KtAnnotationEntry> annotationEntries = target.getAnnotationEntries();
        KtAnnotationEntry req = Utils
                .findAnnotationByName("RequestMapping", annotationEntries);
        if(req == null){
            return "";
        }
        List<? extends ValueArgument> valueArguments = req.getValueArguments();
        if(CollectionUtils.isEmpty(valueArguments)){
            return "";
        }
        ValueArgument argument = valueArguments.get(0);
        return argument.getArgumentExpression().getText();
    }














}
