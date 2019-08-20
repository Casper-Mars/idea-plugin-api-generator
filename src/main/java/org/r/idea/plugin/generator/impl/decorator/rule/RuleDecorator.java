package org.r.idea.plugin.generator.impl.decorator.rule;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiVariable;
import org.r.idea.plugin.generator.core.beans.RuleBO;
import org.r.idea.plugin.generator.impl.Constants;

/**
 * @Author Casper
 * @DATE 2019/8/19 22:21
 **/
public abstract class RuleDecorator {


    /**
     * 修饰参数/属性的限制条件
     *
     * @param rule       限制条件
     * @param annotation 参数/属性注解
     */
    public abstract void decorate(RuleBO rule, PsiAnnotation annotation);


    /**
     * 获取注解的内容
     *
     * @param annotation 注解
     * @param name       内容key名称
     * @return
     */
    protected String getAnnotationValue(PsiAnnotation annotation, String name) {
        PsiNameValuePair[] attributes = annotation.getParameterList().getAttributes();
        if(attributes.length==0||attributes[0]==null){
            return "";
        }
        for (PsiNameValuePair pair : attributes) {
            if (pair.getAttributeName().equals(name)) {
                return pair.getLiteralValue();
            }
        }
        return "";
    }


}
