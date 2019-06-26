package org.r.idea.plugin.generator.gui.util;

import org.r.idea.plugin.generator.gui.ui.component.MyTreeNode;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * @author kuang
 */
public class NodeUtil {
    public static void printChildren(MyTreeNode myTreeNode){
        Enumeration<MyTreeNode> nodes = myTreeNode.children();
        MyTreeNode c = null;
        while(nodes.hasMoreElements()){
            c = nodes.nextElement();
            System.out.println(c.getFilePath());
        }
        nodes = c.children();
        while(nodes.hasMoreElements()) {
            System.out.println(nodes.nextElement().getFilePath());
        }
    }

    /**
     * 根据输入的字符串路径过滤节点
     * TODO 支持windows路径
     * @param root
     * @param inputPath
     */
    public static void filtering(DefaultMutableTreeNode root, String inputPath) {
        String[] pathNameArr = inputPath.split("/");
        List<String> pathNameList = new ArrayList<>();
        for (String s : pathNameArr) {
            if (!"".equals(s)) {
                pathNameList.add(s);
            }
        }
        Enumeration<MyTreeNode> children = root.children();
        filterLevel(children, pathNameList.toArray(new String[pathNameList.size()]), 1);

    }
    private static void filterLevel(Enumeration<MyTreeNode> nodes, String[] pathNameArr, int level){
        if (pathNameArr.length < level) {
            return;
        }
        Enumeration<MyTreeNode> matchedNode = null;
        while(nodes.hasMoreElements()){
            MyTreeNode node = nodes.nextElement();
            matchedNode = filterChildren(node.children(), pathNameArr[level-1]);
        }
        if (matchedNode==null) {
            return;
        }
        filterLevel(matchedNode, pathNameArr, level+1);
    }

    /**
     * 过滤名称不匹配的节点
     * @param nodes
     * @param pathName
     */
    private static Enumeration<MyTreeNode> filterChildren(Enumeration<MyTreeNode> nodes, String pathName){
        Vector<MyTreeNode> nodeEnumeration = new Vector<>();
        MyTreeNode tmp = null;
        while(nodes.hasMoreElements()){
            MyTreeNode node = nodes.nextElement();
            tmp= node;
            if (!node.getTitle().contains(pathName)) {
                DefaultMutableTreeNode pa = (DefaultMutableTreeNode) node.getParent();
                System.out.println(pa.getIndex(node));
                pa.remove(node);
            } else {
                nodeEnumeration.add(node);
            }
        }
        return nodeEnumeration.elements();
    }
}
