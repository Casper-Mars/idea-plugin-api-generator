package org.r.idea.plugin.generator.gui.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;

import java.util.Objects;
import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;
import org.r.idea.plugin.generator.gui.ui.SettingPanel;

/**
 * @ClassName PluginConfig
 * @Author Casper
 * @DATE 2019/6/10 9:56
 **/
public class PluginConfig implements SearchableConfigurable {


    private SettingPanel settingPanel;

    private StorageService storageService = StorageService.getInstance();


    /**
     * Unique configurable id. Note this id should be THE SAME as the one specified in XML.
     */
    @NotNull
    @Override
    public String getId() {
        return "gui-generator";
    }

    /**
     * Returns the visible name of the configurable component. Note, that this method must return the display name that
     * is equal to the display name declared in XML to avoid unexpected errors.
     *
     * @return the visible name of the configurable component
     */
    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return getId();
    }

    /**
     * Creates new Swing form that enables user to configure the settings. Usually this method is called on the EDT, so
     * it should not take a long time.
     * <p>
     * Also this place is designed to allocate resources (subscriptions/listeners etc.)
     *
     * @return new Swing form to show, or {@code null} if it cannot be created
     * @see #disposeUIResources
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if (null == settingPanel) {
            settingPanel = new SettingPanel();
        }
        SettingState state = null;
        if (storageService != null) {
            state = storageService.getState();
        }
        return settingPanel.getPanel(state);
    }

    /**
     * Indicates whether the Swing form was modified or not. This method is called very often, so it should not take a
     * long time.
     *
     * @return {@code true} if the settings were modified, {@code false} otherwise
     */
    @Override
    public boolean isModified() {
        if (storageService == null) {
            return false;
        }
        SettingState state = storageService.getState();
        if (state != null && settingPanel != null) {
            if (!settingPanel.getInterfaceFileText().equals(state.getInterfaceFilePaths())) {
                return true;
            }
            if (!settingPanel.getOutputFileText().equals(state.getOutputFilePaths())) {
                return true;
            }
            if (!settingPanel.getBaseClassText().equals(state.getBaseClass())) {
                return true;
            }
            if (!settingPanel.getMdText().equals(state.getMarkdownFiles())) {
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the settings from the Swing form to the configurable component. This method is called on EDT upon user's
     * request.
     *
     * @throws ConfigurationException if values cannot be applied
     */
    @Override
    public void apply() throws ConfigurationException {
        SettingState state = new SettingState();
        state.setInterfaceFilePaths(settingPanel.getInterfaceFileText());
        state.setOutputFilePaths(settingPanel.getOutputFileText());
        state.setBaseClass(settingPanel.getBaseClassText());
        state.setMarkdownFiles(settingPanel.getMdText());
        storageService.loadState(state);
    }


    /**
     * Loads the settings from the configurable component to the Swing form. This method is called on EDT immediately
     * after the form creation or later upon user's request.
     */
    @Override
    public void reset() {
        if (storageService != null && storageService.getState() != null) {
            settingPanel.init(Objects.requireNonNull(storageService.getState()));
        }
    }
}
