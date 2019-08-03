package org.r.idea.plugin.generator.impl.parser;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.*;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PojoParser
 * @Author Casper
 * @DATE 2019/6/21 11:27
 **/
public class PojoParser {


    public static ParamNode parse(String qualifiedName) throws ClassNotFoundException {

        ParamNode paramNode;
        /*先判断是否为基本类型*/
        if (Utils.isBaseClass(qualifiedName)) {
            paramNode = new ParamNode();
            paramNode.setTypeQualifiedName(qualifiedName);
            paramNode.setEntity(false);
            paramNode.setGenericity(false);
        } else {
            paramNode = parseEntity(qualifiedName);
            EntityContainer.addEntity(qualifiedName, paramNode);
        }
        return paramNode;
    }

    private static ParamNode parseEntity(String qualifiedName) throws ClassNotFoundException {

        /*获取实体类*/
        Project defaultProject = ProjectManager.getInstance().getOpenProjects()[0];
        PsiClass target = Utils.getClass(qualifiedName, defaultProject);

        ParamNode paramNode = new ParamNode();
        paramNode.setEntity(true);
        paramNode.setTypeQualifiedName(qualifiedName);
        /*获取泛型参数*/
        List<String> typeParamList = getTypeParamList(target);
        if (CollectionUtils.isNotEmpty(typeParamList)) {
            paramNode.setGenericityList(typeParamList);
            paramNode.setGenericity(true);
        } else {
            paramNode.setGenericity(false);
            paramNode.setGenericityList(new ArrayList<>());
        }

        PsiClass tmp = target;
        List<Node> children = new ArrayList<>();
        List<String> genericityList = paramNode.getGenericityList();
        while (tmp.getSuperClass() != null) {
            List<Node> tmpList = getChildrenField(tmp, genericityList);
            if (CollectionUtils.isEmpty(tmpList)) {
                continue;
            }
            children.addAll(tmpList);
            genericityList = getSuperRealTypeParamList(tmp);
            tmp = tmp.getSuperClass();
        }
        paramNode.setChildren(children);
        return paramNode;
    }


    private static List<String> getTypeParamList(PsiClass target) {
        PsiTypeParameter[] typeParameters = target.getTypeParameters();
        List<String> result = new ArrayList<>();
        if (typeParameters.length == 0 || typeParameters[0] == null) {
            return result;
        }
        for (PsiTypeParameter parameter : typeParameters) {
            result.add(parameter.getText());
        }
        return result;
    }

    private static List<String> getSuperRealTypeParamList(PsiClass target) {

        List<String> result = new ArrayList<>();
        PsiClassType[] extendsListTypes = target.getExtendsListTypes();

        if (extendsListTypes.length == 0 || extendsListTypes[0] == null) {
            return result;
        }
        for (PsiClassType type : extendsListTypes) {
            PsiType[] parameters = type.getParameters();
            if (parameters.length == 0 || parameters[0] == null) {
                continue;
            }
            for (PsiType parameter : parameters) {
                result.add(parameter.getCanonicalText());
            }
        }
        return result;
    }

    private static List<Node> getChildrenField(PsiClass target, List<String> paramterTypeList) throws ClassNotFoundException {
        List<Node> children = new ArrayList<>();
        PsiField[] fields = target.getFields();
        List<String> typeParamList = getTypeParamList(target);
        for (PsiField field : fields) {
            String type = field.getType().getCanonicalText();
            ParamNode child = new ParamNode();
            child.setTypeQualifiedName(type);
            ObjectParser.decorate(child, typeParamList);
            int i = -1;
            if ((i = typeParamList.indexOf(child.getTypeQualifiedName())) != -1) {
                child.setTypeQualifiedName(paramterTypeList.get(i));
            }
            child.setName(field.getName());
            if (field.getDocComment() == null) {
                child.setDesc("");
            } else {
                child.setDesc(Utils.getDocCommentDesc(field.getDocComment().getDescriptionElements()));
            }
            children.add(child);
        }
        return children;
    }


}
