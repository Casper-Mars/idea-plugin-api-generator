package org.r.idea.plugin.generator.impl.indicators;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import org.r.idea.plugin.generator.core.indicators.InterfaceIndicator;
import org.r.idea.plugin.generator.impl.Utils;

/**
 * @ClassName InterfaceIndicatorImpl
 * @Author Casper
 * @DATE 2019/6/22 14:59
 **/
public class InterfaceIndicatorImpl implements InterfaceIndicator {


    private String flag = "org.springframework.web.bind.annotation.RestController";


    @Override
    public boolean isInterface(PsiClass psiClass) {
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        return  annotations.length>0 && Utils.isContainAnnotation(flag,psiClass.getAnnotations());
    }
}
