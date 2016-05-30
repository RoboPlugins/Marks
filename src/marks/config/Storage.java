package marks.config;


import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@State(name = "MarkSettings",
        storages = {@com.intellij.openapi.components.Storage(
                file = StoragePathMacros.APP_CONFIG + "/plugin_settings.xml")})

public class Storage implements PersistentStateComponent<Storage> {

    private Integer mRegionRGB;

    @Nullable
    @Override
    public Storage getState() {
        PluginManager.getLogger().info("getState");
        return this;
    }

    @Override
    public void loadState(Storage state) {
        PluginManager.getLogger().info("loadState");
        XmlSerializerUtil.copyBean(state, this);
    }

    public Integer getRegionColor() {
        return mRegionRGB;
    }

    void setRegionColor(Color regionColor) {
        mRegionRGB = regionColor.getRGB();
    }
}
