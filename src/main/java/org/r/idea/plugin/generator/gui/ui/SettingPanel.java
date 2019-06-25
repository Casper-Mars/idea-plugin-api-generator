package org.r.idea.plugin.generator.gui.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.r.idea.plugin.generator.gui.util.FilePathUtil;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @ClassName SettingPanel
 * @Author Casper
 * @DATE 2019/6/12 17:33
 **/
public class SettingPanel {

    private JPanel main;
    private JButton add;
    private JButton del;
    private JTable pathTable;
    private JTextField workSpace;
    private JButton wsBut;
    private JTextField baseClass;
    private JTextField mdText;
    private JButton mdBut;

    public JComponent getPanel(SettingState state) {

        initTable();
        initButton();
        if (state != null) {
            init(state);
        }
        return main;
    }


    public void initTable() {
        DefaultTableModel model = (DefaultTableModel) pathTable.getModel();
        Vector<String> header = new Vector<>();
        header.add("路径");
        model.setColumnIdentifiers(header);
        pathTable.setModel(model);
    }

    public void initButton() {
        add.addActionListener(e -> {
            List<String> paths = selectPaths();
            setPath(paths);
        });
        del.addActionListener(e -> {
            DefaultTableModel tmp = (DefaultTableModel) pathTable.getModel();
            int[] selectedRows = pathTable.getSelectedRows();
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                tmp.removeRow(selectedRows[i]);
            }
            pathTable.setModel(tmp);
        });

        wsBut.addActionListener(e -> {
            List<String> paths = selectPaths();
            String path = paths.get(0);
            if (StringUtils.isNotEmpty(path)) {
                setOutputFileText(path);
            }
        });
        mdBut.addActionListener(e -> {
            List<String> paths = selectPaths();
            String path = paths.get(0);
            if (StringUtils.isNotEmpty(path)) {
                setMdText(path);
            }
        });
    }

    public void init(SettingState state) {
        setInterfaceFileText(state.getInterfaceFilePaths());
        setOutputFileText(state.getOutputFilePaths());
        setBaseClassText(state.getBaseClass());
        setMdText(state.getMarkdownFiles());
    }


    private List<String> selectPaths() {
        Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        Project curProject = null;
        if (openProjects.length > 0) {
            curProject = openProjects[0];
        }
        List<String> result = new ArrayList<>();
        FileChooser.chooseFiles(
                new FileChooserDescriptor(false, true, false, false, false, true),
                curProject,
                null,
                t -> {
                    for (int i = 0; i < t.size(); i++) {
                        result.add(FilePathUtil.formatPath(t.get(i).getPath()));
                    }
                });
        return result;
    }


    public void setPath(List<String> srcPath) {
        if (srcPath == null) {
            return;
        }
        /*获取现有的路径*/
        String path = getPath();
        /*如果现有的路径存在，则先过滤重复的*/
        if (StringUtils.isNotEmpty(path)) {
            List<String> tmp = new ArrayList<>(Arrays.asList(path.split(Constants.SPLITOR)));
            srcPath.removeAll(tmp);
        }
        DefaultTableModel model = (DefaultTableModel) pathTable.getModel();
        for (String s : srcPath) {
            Vector<String> tmp = new Vector<>();
            tmp.add(s);
            model.addRow(tmp);
        }
        pathTable.setModel(model);
    }

    public String getPath() {
        TableModel model = pathTable.getModel();
        int rowCount = model.getRowCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rowCount; i++) {
            sb.append(model.getValueAt(i, 0)).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    public String getInterfaceFileText() {
        return getPath();
    }

    public void setInterfaceFileText(String interfaceFileText) {
        if (isNotVaildPath(interfaceFileText)) {
            return;
        }
        setPath(new ArrayList<>(Arrays.asList(interfaceFileText.split(Constants.SPLITOR))));
    }

    public String getOutputFileText() {
        return workSpace.getText();
    }

    public void setOutputFileText(String text) {
        workSpace.setText(text);
    }

    public String getBaseClassText() {
        return baseClass.getText();
    }

    public void setBaseClassText(String text) {
        baseClass.setText(text);
    }

    public String getMdText() {
        return mdText.getText();
    }

    public void setMdText(String mdText) {
        this.mdText.setText(mdText);
    }

    private boolean isNotVaildPath(String path) {
        return StringUtils.isEmpty(path);
    }


}
