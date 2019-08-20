package org.r.idea.plugin.generator.impl.decorator.rule;

import com.intellij.psi.PsiAnnotation;
import org.r.idea.plugin.generator.core.beans.RuleBO;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.math.BigDecimal;

public class DecimalMinDecorator extends RuleDecorator {
    /**
     * 修饰参数/属性的限制条件
     *
     * @param rule       限制条件
     * @param annotation 参数/属性注解
     */
    @Override
    public void decorate(RuleBO rule, PsiAnnotation annotation) {
        String value = getAnnotationValue(annotation, "value");
        if (StringUtils.isNotEmpty(value)) {
            rule.setMin(new BigDecimal(value));
        }
    }
}
