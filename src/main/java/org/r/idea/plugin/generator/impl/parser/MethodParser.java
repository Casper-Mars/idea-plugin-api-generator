package org.r.idea.plugin.generator.impl.parser;

import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNameValuePair;

import java.util.List;

import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.enums.RequestMethodEnum;
import org.r.idea.plugin.generator.impl.nodes.MethodNode;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName MethodParser
 * @Author Casper
 * @DATE 2019/6/21 17:38
 **/
public class MethodParser {


    public MethodNode parse(PsiMethod target) throws ClassNotFoundException {

        MethodNode methodNode = new MethodNode();
        /*判断该方法是否为接口，如果是，则设置好方法的url和请求方法*/
        if (!isAvailable(methodNode, target)) {
            return null;
        }
        /*设置描述*/
        methodNode.setDesc(Utils.getDocCommentDesc(target.getDocComment()));
        /*设置方法名*/
        methodNode.setName(target.getName());
        /*处理参数和返回值*/
        try {
            processParameterAndRespone(methodNode, target);
        } catch (ClassNotFoundException e) {
            e.setMsg(target.getName() + "-" + e.getMsg());
            throw e;
        }
        return methodNode;
    }


    private boolean isAvailable(MethodNode node, PsiMethod target) {

        PsiAnnotation[] annotations = target.getAnnotations();

        /*判断接口是否可用*/
        boolean isAvailable = !Utils.isContainAnnotation("java.lang.Deprecated", annotations);

        if (isAvailable) {
            PsiAnnotation req = null;
            RequestMethodEnum targetMethod = null;
            for (RequestMethodEnum methodEnum : RequestMethodEnum.getSingle()) {
                req = Utils.findAnnotationByName(methodEnum.getAnnotation(), annotations);
                if (req != null) {
                    targetMethod = methodEnum;
                    break;
                }
            }
            /*如果上面的找不到对应的请求注解，则尝试找原始的请求注解*/
            if (req == null) {
                req = Utils.findAnnotationByName(RequestMethodEnum.ALL.getAnnotation(), annotations);
                targetMethod = RequestMethodEnum.ALL;
            }
            /*如果两种注解都找不到，则不是接口直接返回*/
            if (req == null) {
                return false;
            }
            /*否则进行url和请求方法的处理*/
            processUrlAndMethod(node, req, targetMethod);
            return true;
        }
        return false;
    }


    private void processUrlAndMethod(MethodNode methodNode, PsiAnnotation annotation, RequestMethodEnum methodEnum) {

        for (PsiNameValuePair pair : annotation.getParameterList().getAttributes()) {
            if (pair.getAttributeName().equals(Constants.REQUESTMAPPING_ATTR_VALUE)) {
                methodNode.setUrl(pair.getLiteralValue());
            }
            if (pair.getAttributeName().equals(Constants.REQUESTMAPPING_ATTR_METHOD)) {
                methodNode.setRequestType(splitMethod(pair.getLiteralValue()));
            }
        }
        /*如果注解中没有指定请求方法，则取枚举定义好的*/
        if (StringUtils.isEmpty(methodNode.getRequestType())) {
            methodNode.setRequestType(methodEnum.getValue());
        }
    }

    private void processParameterAndRespone(MethodNode node, PsiMethod target) throws ClassNotFoundException {
        ParamParser paramParser = new ParamParser();
        node.setChildren(paramParser.parse(target));
        ResponeParser responeParser = new ResponeParser();
        node.setResponed(responeParser.parse(target));
    }

    /**
     * 转化形如{RequestMethod.POST,RequestMethod.GET}为POST/GET的字符串
     */
    private String splitMethod(String method) {
        if (StringUtils.isEmpty(method)) {
            return "";
        }
        /*去掉两边的“{}”，再按照“，”分割*/
        String[] split = method.replace("{", "").replace("}", "").split(Constants.SPLITOR);
        StringBuilder sb = new StringBuilder();
        for (String tmp : split) {
            sb.append(tmp.split("\\.")[1]).append("/");
        }
        /*清楚最后一个多余的"/"*/
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


}
