package org.r.idea.plugin.generator.core.indicators;

import com.intellij.psi.PsiClass;

/**
 * @ClassName InterfaceIndicator
 * @Author Casper
 * @DATE 2019/6/22 14:58
 **/
public interface InterfaceIndicator {

    static InterfaceIndicator getInstance() {
        return IndicatorFactory.get(InterfaceIndicator.class);
    }

    boolean isInterface(PsiClass psiClass);


}
