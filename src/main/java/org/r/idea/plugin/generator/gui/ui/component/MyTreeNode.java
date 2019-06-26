package org.r.idea.plugin.generator.gui.ui.component;

import com.intellij.icons.AllIcons;
import java.io.File;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @ClassName MyTreeNode
 * @Author Casper
 * @DATE 2019/6/13 12:44
 **/
public class MyTreeNode extends DefaultMutableTreeNode {


    private String title;
    private String filePath;
    private boolean hasChild;
    private Icon icon;


    /**
     * Creates a tree node that has no parent and no children, but which allows children.
     */
    public MyTreeNode(String title, File file) {
        this.title = title;
        this.filePath = file.getPath();
        if (file.isDirectory()) {
            this.hasChild = file.listFiles() != null && file.listFiles().length > 0;
            this.icon = AllIcons.Nodes.Folder;
        } else {
            this.hasChild = false;
            this.icon = AllIcons.FileTypes.Any_type;
        }
        init();
    }

    private void init() {
        if (hasChild) {
            add(new DefaultMutableTreeNode());
        }
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
