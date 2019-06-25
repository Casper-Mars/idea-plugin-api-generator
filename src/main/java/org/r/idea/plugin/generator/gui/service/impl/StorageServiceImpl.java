package org.r.idea.plugin.generator.gui.service.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.r.idea.plugin.generator.gui.beans.SettingState;
import org.r.idea.plugin.generator.gui.service.StorageService;

/**
 * @ClassName StorageServiceImpl
 * @Author Casper
 * @DATE 2019/6/10 9:44
 **/
@State(name = "api-generator setting", storages = @Storage("api_generator_plugin.xml"))
public class StorageServiceImpl implements StorageService {


    private SettingState settingState;

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates that
     * the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Nullable
    @Override
    public SettingState getState() {
        return settingState;
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * Config files were externally changed while IDE was running.
     * <p>
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see XmlSerializerUtil#copyBean(Object, Object)
     */
    @Override
    public void loadState(@NotNull SettingState state) {
        settingState = state;
    }


    /**
     * This method is called when the component is initialized, but no state is persisted.
     */
    @Override
    public void noStateLoaded() {
        settingState = SettingState.getDefault();
    }
}
