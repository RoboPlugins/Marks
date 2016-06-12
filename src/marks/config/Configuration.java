package marks.config;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Configuration implements Configurable, ChangeListener {

    private MarkSettingsStorage mStorage = ServiceManager.getService(MarkSettingsStorage.class);
    private Integer mRegionColor = 8089544;
    private Color defaultRegionJBColor = new JBColor(mRegionColor, mRegionColor);
    boolean mModified = false;
    private JColorChooser tcc;
    @Nls
    @Override
    public String getDisplayName() {
        return "Marks";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Marks";
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        if(mStorage.getRegionColor() != null) {
            Integer regionRGB = mStorage.getRegionColor();
            mRegionColor = mStorage.getRegionColor();
        }

        // Create a message
        JPanel mainPaine = new JPanel();
        //Set up the banner at the top of the window

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));

        //Set up color chooser for setting text color
        tcc = new JColorChooser(new JBColor(mRegionColor, mRegionColor));
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                "Choose Text Color"));

        mainPaine.add(bannerPanel, BorderLayout.CENTER);
        mainPaine.add(tcc, BorderLayout.PAGE_END);

        return mainPaine;
    }

    @Override
    public boolean isModified() {
        return mModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        Integer regionColor = getRegionColor();
        PluginManager.getLogger().warn("1 apply mRegionColor:" + mRegionColor);
        mStorage.setRegionColor(mRegionColor);
        PluginManager.getLogger().warn("2 apply mRegionColor:" + mRegionColor);
        ApplicationManager.getApplication().saveAll();
        PluginManager.getLogger().warn("3 apply mRegionColor:" + mRegionColor);
        mModified = false;
    }

    @Override
    public void reset() {
        PluginManager.getLogger().info("reset");
        Integer regionColor = mStorage.getRegionColor();
        if(regionColor != null) {
            mRegionColor = regionColor;
            PluginManager.getLogger().warn("reset regionColor != null: " + mRegionColor);
        }
    }

    @Override
    public void disposeUIResources() {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        mModified = true;
        mStorage.setRegionColor(newColor.getRGB());
        mRegionColor = getRegionColor();
        PluginManager.getLogger().warn("stateChanged: " + mStorage.getRegionColor());
    }

    public Integer getRegionColor() {
        PluginManager.getLogger().info("reset");
        Integer regionColor = mStorage.getRegionColor();
        if(regionColor != null) {
            mRegionColor = regionColor;
        }
        PluginManager.getLogger().info("getRegionColor: " + mStorage.getRegionColor());
        return mRegionColor;
    }
}