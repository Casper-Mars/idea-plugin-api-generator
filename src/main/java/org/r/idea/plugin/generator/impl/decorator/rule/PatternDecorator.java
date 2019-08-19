package org.r.idea.plugin.generator.impl.decorator.rule;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiVariable;
import org.r.idea.plugin.generator.core.beans.RuleBO;
import org.r.idea.plugin.generator.impl.Utils;

/**
 * @Author Casper
 * @DATE 2019/8/19 22:23
 **/
public class PatternDecorator implements RuleDecorator {

    /**
     * 修饰参数/属性的限制条件
     *
     * @param rule      限制条件
     * @param parameter 参数/属性
     */
    @Override
    public void decorate(RuleBO rule, PsiVariable parameter) {

        PsiAnnotation annotation = Utils.findAnnotationByName("javax.validation.constraints.Pattern", parameter.getAnnotations());

    }
}
