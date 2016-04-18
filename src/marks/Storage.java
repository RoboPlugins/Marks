package marks;


import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(name = "PainPointSettings",
        storages = {@com.intellij.openapi.components.Storage(
                file = StoragePathMacros.APP_CONFIG + "/plugin_settings.xml")})

public class Storage implements PersistentStateComponent<Storage> {

    private String mH2Url = "";

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

    public String getH2Url() {
        return mH2Url;
    }

    public void setH2Url(String h2Url) {
        mH2Url = h2Url;
    }
}
