//package org.r.idea.plugin.generator.ktimpl;
//
//import org.jetbrains.kotlin.kdoc.psi.api.KDoc;
//import org.jetbrains.kotlin.kdoc.psi.impl.KDocSection;
//import org.jetbrains.kotlin.psi.KtAnnotationEntry;
//import org.r.idea.plugin.generator.utils.CollectionUtils;
//
//import java.util.List;
//
///**
// * kotlin的工具类
// *
// * @author casper
// */
//public class Utils {
//
//
//    /**
//     * 根据给定的注释集合构造注释字符串
//     *
//     * @param doc 给定的注释
//     */
//    public static String getDocCommentDesc(KDoc doc) {
//
//        if (doc == null) {
//            return "";
//        }
//        KDocSection defaultSection = doc.getDefaultSection();
//        return defaultSection.getContent();
//    }
//
//    /**
//     * 在指定的注解集合中找出指定的注解
//     *
//     * @param qualifiedName 指定的注解全名称
//     * @param src           指定的注解集合
//     */
//    public static KtAnnotationEntry findAnnotationByName(String qualifiedName, List<KtAnnotationEntry> src) {
//        if (CollectionUtils.isEmpty(src)) {
//            return null;
//        }
//        for (KtAnnotationEntry annotation : src) {
//            if (qualifiedName.equals(annotation.getShortName().asString())) {
//                return annotation;
//            }
//        }
//        return null;
//    }
//
//}
