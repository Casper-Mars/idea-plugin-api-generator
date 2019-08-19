package org.r.idea.plugin.generator.impl.decorator.rule;

import com.intellij.psi.PsiVariable;
import org.r.idea.plugin.generator.core.beans.RuleBO;

/**
 * @Author Casper
 * @DATE 2019/8/19 22:21
 **/
public interface RuleDecorator {


    /**
     * 修饰参数/属性的限制条件
     *
     * @param rule      限制条件
     * @param parameter 参数/属性
     */
    void decorate(RuleBO rule, PsiVariable parameter);


}
