package org.r.idea.plugin.generator.impl.parser;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.ArrayList;
import java.util.List;
import org.r.idea.plugin.generator.core.exceptions.ClassNotFoundException;
import org.r.idea.plugin.generator.core.indicators.GenericityIndicator;
import org.r.idea.plugin.generator.core.indicators.InterfaceIndicator;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.core.indicators.IndicatorFactory;
import org.r.idea.plugin.generator.core.nodes.Node;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;

/**
 * @ClassName PojoParser
 * @Author Casper
 * @DATE 2019/6/21 11:27
 **/
public class PojoParser {

    private GenericityIndicator genericityIndicator = GenericityIndicator.getInstance();

    public ParamNode parse(String qualifiedName) throws ClassNotFoundException {

        ParamNode paramNode;

        List<String> typeParams = new ArrayList<>();
        /*判断是否泛型*/
        if (!genericityIndicator.isGenricityType(qualifiedName, typeParams)) {
            /*非泛型*/
            String type = isArray(qualifiedName);
            boolean isArray = type.length() < qualifiedName.length();
            paramNode = parserPojo(type);
            paramNode.setArray(isArray);

        } else if (typeParams.size() == 1) {
            /*只处理一元的泛型，并默认为list*/
            String type = typeParams.get(0);
            paramNode = parserPojo(type);
            paramNode.setArray(true);
        } else {
            /*其他高元泛型不支持默认为object*/
            paramNode = new ParamNode();

            paramNode.setTypeQualifiedName("java.lang.Object");
        }

        return paramNode;
    }

    private String isArray(String type) {
        if (type.contains(Constants.ARRAYFLAG)) {
            return type.replace(Constants.ARRAYFLAG, "");
        } else {
            return type;
        }
    }


    private ParamNode parserPojo(String qualifiedName) throws ClassNotFoundException {
        ParamNode paramNode = new ParamNode();
        /*先判断是否为基本类型*/
        if (Utils.isBaseClass(qualifiedName)) {
            paramNode.setTypeQualifiedName(qualifiedName);
        } else {
            Project defaultProject = ProjectManager.getInstance().getOpenProjects()[0];
            PsiClass target = Utils.getClass(qualifiedName, defaultProject);
            paramNode.setJson(true);
            paramNode.setEntity(true);
            paramNode.setTypeQualifiedName(target.getQualifiedName());
            List<Node> children = new ArrayList<>();
            for (PsiField field : target.getFields()) {
                /*如果实体类出现自引用则不解析*/
                if (field.getType().getCanonicalText().equals(target.getQualifiedName())) {
                    continue;
                }
                ParamNode child = parse(field.getType().getCanonicalText());
                child.setName(field.getName());
                if (field.getDocComment() == null) {
                    child.setDesc("");
                } else {
                    child.setDesc(Utils.getDocCommentDesc(field.getDocComment().getDescriptionElements()));
                }
                children.add(child);
            }
            paramNode.setChildren(children);
        }
        return paramNode;
    }


}
