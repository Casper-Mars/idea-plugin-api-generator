package org.r.idea.plugin.generator.impl;


import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.kotlin.kdoc.psi.api.KDoc;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;
import org.r.idea.plugin.generator.utils.CollectionUtils;

import javax.sql.rowset.BaseRowSet;

/**
 * @ClassName Utils
 * @Author Casper
 * @DATE 2019/6/21 14:31
 **/
public class Utils {


    public static String[] baseClass = {};


    /**
     * 判断指定的额类名是否属于基本类
     *
     * @param classShortName 给定的类名
     */
    public static boolean isBaseClass(String classShortName) {
        for (String tmp : baseClass) {
            if (classShortName.contains(tmp)) {
                return true;
            }
        }
        return false;
    }

    public static String isArrayType(String qualifiedName) {
        if (qualifiedName.contains(Constants.ARRAYFLAG)) {
            qualifiedName = qualifiedName.replace(Constants.ARRAYFLAG, "");
        } else if (qualifiedName.contains(Constants.LISTFLAG)) {
            int start = qualifiedName.indexOf(Constants.LISTFLAG);
            if (start == 0) {
                int end = qualifiedName.lastIndexOf('>');
                qualifiedName = qualifiedName.substring(start + Constants.LISTFLAG.length(), end);
            }
        }
        return qualifiedName;
    }

    public static String getType(ParamNode node) {
        if (node == null) return "";
        StringBuilder sb = new StringBuilder();
        String typeShortName = node.getTypeShortName();

        sb.append(typeShortName);
        if (node.isGenericity()) {
            sb.append('<');
            if (CollectionUtils.isNotEmpty(node.getGenericityList())) {
                for (int i = 0; i < node.getGenericityList().size(); i++) {
//                    sb.append(getType(node.getGenericityList().get(i)));
                    sb.append(node.getGenericityList().get(i));
                    if (i < node.getGenericityList().size() - 1) {
                        sb.append(',');
                    }
                }
            }
            sb.append('>');
        }
        if (node.isArray()) {
            sb.append("[]");
        }
        return sb.toString();
    }


    /**
     * 根据给定的注释集合构造注释字符串
     *
     * @param doc 给定的注释
     */
    public static String getDocCommentDesc(PsiDocComment doc) {

        if (doc == null) {
            return "";
        }
        PsiElement[] desc = doc.getDescriptionElements();
        StringBuilder sb = new StringBuilder();
        if (desc.length == 0 || desc[0] == null) {
            return "";
        }
        for (PsiElement tmp : desc) {
            if (tmp instanceof PsiDocToken) {
                sb.append(tmp.getText());
            }
        }
        return sb.toString();
    }




    /**
     * 判断指定的注解集合中是否包括指定的注解
     *
     * @param qualifiedName 指定的注解全名称
     * @param src           指定的注解集合
     */
    public static boolean isContainAnnotation(String qualifiedName, PsiAnnotation[] src) {
        return findAnnotationByName(qualifiedName, src) != null;
    }

    /**
     * 在指定的注解集合中找出指定的注解
     *
     * @param qualifiedName 指定的注解全名称
     * @param src           指定的注解集合
     */
    public static PsiAnnotation findAnnotationByName(String qualifiedName, PsiAnnotation[] src) {
        if (null == src || qualifiedName == null || src.length == 0) {
            return null;
        }
        for (PsiAnnotation annotation : src) {
            if (qualifiedName.equals(annotation.getQualifiedName())) {
                return annotation;
            }
        }
        return null;
    }

    public static PsiClass getClass(String qualifiedName, Project project) throws ClassNotFoundException {
        PsiClass target = JavaPsiFacade.getInstance(project)
                .findClass(qualifiedName, GlobalSearchScope.allScope(project));
        if (target == null) {
            throw new ClassNotFoundException("不存在类：" + qualifiedName);
        }
        return target;
    }


    /**
     * 判断参数是否必传
     * javax.validation.constraints.NotNull
     *
     * @param parameter
     */
    public static boolean isRequire(PsiVariable parameter) {

        return Utils.isContainAnnotation("javax.validation.constraints.NotNull", parameter.getAnnotations());
    }

}
