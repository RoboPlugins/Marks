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

    private Storage mStorage = ServiceManager.getService(Storage.class);
    private Color mRegionColor;
    private Color defaultRegionColor = new JBColor(8089544, 8089544);
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
            int regionRGB = mStorage.getRegionColor();
            mRegionColor = new JBColor(regionRGB, regionRGB);
        } else {
            mRegionColor = defaultRegionColor;
        }

        // Create a message
        JPanel mainPaine = new JPanel();
        //Set up the banner at the top of the window

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));

        //Set up color chooser for setting text color
        tcc = new JColorChooser(mRegionColor);
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
        PluginManager.getLogger().info("apply");
        ApplicationManager.getApplication().saveAll();
        mStorage.setRegionColor(mRegionColor);
        mModified = false;
    }

    @Override
    public void reset() {
        PluginManager.getLogger().info("reset");
        Integer regionColor = mStorage.getRegionColor();
        if(regionColor != null) {
            mRegionColor = new JBColor(regionColor, regionColor);
        } else {
            mRegionColor = defaultRegionColor;
        }
    }

    @Override
    public void disposeUIResources() {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        mModified = true;
        mStorage.setRegionColor(newColor);
    }
    public JBColor getRegionColor() {
        PluginManager.getLogger().info("reset");
        Integer regionColor = mStorage.getRegionColor();
        if(regionColor != null) {
            mRegionColor = new JBColor(regionColor, regionColor);
        } else {
            mRegionColor = defaultRegionColor;
        }
        return new JBColor(mRegionColor, mRegionColor);
    }
}