package org.r.idea.plugin.generator.gui.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.r.idea.plugin.generator.gui.beans.SettingState;

/**
 * @ClassName StorageService
 * @Author Casper
 * @DATE 2019/6/10 9:44
 **/
public interface StorageService extends PersistentStateComponent<SettingState> {

    static StorageService getInstance() {
        Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        if (openProjects.length > 0) {
            return openProjects[0].getComponent(StorageService.class);
        }
        return null;
    }

}
