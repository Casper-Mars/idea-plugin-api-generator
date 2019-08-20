package org.r.idea.plugin.generator.impl.service;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiVariable;
import org.r.idea.plugin.generator.core.beans.RuleBO;
import org.r.idea.plugin.generator.impl.decorator.rule.DecimalMaxDecorator;
import org.r.idea.plugin.generator.impl.decorator.rule.DecimalMinDecorator;
import org.r.idea.plugin.generator.impl.decorator.rule.PatternDecorator;
import org.r.idea.plugin.generator.impl.decorator.rule.RuleDecorator;
import org.r.idea.plugin.generator.utils.StringUtils;

public class RuleService {

    private static RuleService ruleService;

    public static RuleService getInstance() {

        synchronized (RuleService.class) {
            if (ruleService == null) {
                ruleService = new RuleService();
            }
        }
        return ruleService;
    }


    /**
     * 根据参数生成限制条件
     *
     * @param parameter 参数
     * @return
     */
    public RuleBO getRule(PsiVariable parameter) {
        RuleBO ruleBO = new RuleBO();
        /*判断参数，确保健壮性*/
        if (parameter == null) {
            return ruleBO;
        }
        /*获取参数注解*/
        PsiAnnotation[] annotations = parameter.getAnnotations();
        if (annotations.length == 0 || annotations[0] == null) {
            return ruleBO;
        }
        /*遍历注解修饰限制条件*/
        for (PsiAnnotation annotation : annotations) {
            RuleDecorator decorator = getDecorator(annotation);
            if (decorator == null) {
                continue;
            }
            try {
                decorator.decorate(ruleBO, annotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ruleBO;
    }


    /**
     * 根据注解获取对应的修饰者
     *
     * @param annotation 注解
     * @return
     */
    private RuleDecorator getDecorator(PsiAnnotation annotation) {
        String qualifiedName = annotation.getQualifiedName();
        if (StringUtils.isEmpty(qualifiedName)) {
            return null;
        }
        RuleDecorator ruleDecorator = null;
        switch (qualifiedName) {
            case "javax.validation.constraints.Pattern":
                ruleDecorator = new PatternDecorator();
                break;
            case "javax.validation.constraints.DecimalMax":
                ruleDecorator = new DecimalMaxDecorator();
                break;
            case "javax.validation.constraints.DecimalMin":
                ruleDecorator = new DecimalMinDecorator();
                break;
            default:
                break;
        }
        return ruleDecorator;
    }


}
