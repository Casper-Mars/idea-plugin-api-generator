package org.r.idea.plugin.generator.core.parser;

import com.intellij.psi.PsiElement;
import org.r.idea.plugin.generator.core.config.ServerManager;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.nodes.Node;

/**
 * @ClassName Parser
 * @Author Casper
 * @DATE 2019/6/21 16:41
 **/

public interface Parser {

    static Parser getInstance() {
        return ServerManager.getServer(Parser.class);
    }

    /**
     * 根据class对象转化出信息
     *
     * @param target class对象
     * @return
     * @throws ClassNotFoundException
     */
    Node parse(PsiElement target) throws ClassNotFoundException;

}
