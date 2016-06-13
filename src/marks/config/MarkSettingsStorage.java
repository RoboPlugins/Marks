package marks.config;


import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@State(name = "MarkSettings",
        storages = {
                @Storage(
                        file = StoragePathMacros.APP_CONFIG + "/marks.xml",
                        id = "Marks"
                )
        }
)

public class MarkSettingsStorage implements PersistentStateComponent<MarksPluginState> {

    private MarksPluginState pluginState = new MarksPluginState();

    @Nullable
    @Override
    public MarksPluginState getState() {
        PluginManager.getLogger().warn("getState regionColor: " + pluginState.getRegionRGB());
        return pluginState;
    }

    @Override
    public void loadState(MarksPluginState element) {
        PluginManager.getLogger().warn("loadState");
        pluginState = element;
//        XmlSerializerUtil.copyBean(element, this);
    }

    public Integer getRegionColor() {
        return pluginState.getRegionRGB();
    }

    void setRegionColor(Integer regionColor) {
        pluginState.setRegionRGB(regionColor);
        PluginManager.getLogger().warn("setRegionColor regionColor: " + pluginState.getRegionRGB());

    }
}
