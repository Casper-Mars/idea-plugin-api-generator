package org.r.idea.plugin.generator.core.parser;

import com.intellij.psi.PsiClass;
import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName Parser
 * @Author Casper
 * @DATE 2019/6/21 16:41
 **/

public interface Parser {


    public Node parse(PsiClass target);

}
