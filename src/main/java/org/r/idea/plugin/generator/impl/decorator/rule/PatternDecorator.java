package org.r.idea.plugin.generator.impl.decorator.rule;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiNameValuePair;
import org.r.idea.plugin.generator.core.beans.RuleBO;

/**
 * @Author Casper
 * @DATE 2019/8/19 22:23
 **/
public class PatternDecorator extends RuleDecorator {

    /**
     * 修饰参数/属性的限制条件
     *
     * @param rule       限制条件
     * @param annotation 参数/属性注解
     */
    @Override
    public void decorate(RuleBO rule, PsiAnnotation annotation) {
        rule.setPattern(getAnnotationValue(annotation, "regexp"));
    }
}
