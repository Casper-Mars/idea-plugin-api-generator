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


    public static ParamNode parseEntity(String qualifiedName) throws ClassNotFoundException {

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

        List<Node> children = new ArrayList<>();
        paramNode.setChildren(children);
        PsiField[] fields = target.getFields();
        if (fields.length == 0 || fields[0] == null) {
            return paramNode;
        }
        for (PsiField field : fields) {
            ParamNode child = new ParamNode();
            child.setTypeQualifiedName(field.getType().getCanonicalText());
            child.setName(field.getName());
            children.add(child);
        }
        paramNode.setSuperClass(getSuperClassName(target));
        return paramNode;
    }

    private static String getSuperClassName(PsiClass target) {
        PsiClass superClass = target.getSuperClass();
        if (superClass == null) {
            return "";
        }
        String superClassName = superClass.getQualifiedName();
        String s = getSuperRealTypeParamList(target);
        return superClassName + s;
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

    private static String getSuperRealTypeParamList(PsiClass target) {

        PsiClassType[] extendsListTypes = target.getExtendsListTypes();

        if (extendsListTypes.length == 0 || extendsListTypes[0] == null) {
            return "";
        }
        PsiClassType extendsListType = extendsListTypes[0];
        PsiType[] typeParameters = extendsListType.getParameters();
        if (typeParameters.length == 0 || typeParameters[0] == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        for (int i = 0; i < typeParameters.length; i++) {
            sb.append(typeParameters[i].getCanonicalText());
            if (i < typeParameters.length - 1) {
                sb.append(',');
            }
        }
        sb.append('>');
        return sb.toString();
    }

//    private static List<Node> getChildrenField(PsiClass target, List<ParamNode> paramterTypeList) throws ClassNotFoundException {
//        List<Node> children = new ArrayList<>();
//        PsiField[] fields = target.getFields();
//        List<ParamNode> typeParamList = getTypeParamList(target);
//        List<String> typeParamStrList = typeParamList.stream().map(ParamNode::getTypeQualifiedName).collect(Collectors.toList());
//        boolean hasParamType = CollectionUtils.isNotEmpty(paramterTypeList);
//        for (PsiField field : fields) {
//            String type = field.getType().getCanonicalText();
//            ParamNode child = new ParamNode();
//            child.setTypeQualifiedName(type);
//            ObjectParser.decorate(child, typeParamStrList);
//            int i = -1;
//            if (hasParamType && (i = typeParamStrList.indexOf(child.getTypeQualifiedName())) != -1) {
//                child.setTypeQualifiedName(Utils.getType(paramterTypeList.get(i)));
//            }
//            child.setName(field.getName());
//            if (field.getDocComment() == null) {
//                child.setDesc("");
//            } else {
//                child.setDesc(Utils.getDocCommentDesc(field.getDocComment().getDescriptionElements()));
//            }
//            children.add(child);
//        }
//        return children;
//    }


}
