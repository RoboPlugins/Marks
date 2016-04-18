package marks;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Configuration implements Configurable {

    private Storage mStorage = ServiceManager.getService(Storage.class);
    private JTextField mTextField;
    private String mH2Url = "";
    private String sharedUrl = "jdbc:h2:tcp://localhost/~/test";
    private String defaultUrl = "jdbc:h2:tcp://10.12.22.97/~/test";
    boolean mModified = false;
    @Nls
    @Override
    public String getDisplayName() {
        return "Pain Points";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Pain Points";
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        if(!mStorage.getH2Url().isEmpty()) {
            mH2Url = mStorage.getH2Url();
        } else {
            mH2Url = defaultUrl;
        }

        // Create a message
        JPanel mainPaine = new JPanel();
        VerticalLayout verticalLayout = new VerticalLayout(SwingConstants.HORIZONTAL);
        mainPaine.setLayout(verticalLayout);
        mTextField = new JTextField();
        mTextField.setText(mH2Url);
        mTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urlValue = e.getActionCommand();
                PluginManager.getLogger().error("Action Performed: " + urlValue);
                mH2Url = mTextField.getText();
                mModified = true;
            }
        });
        mTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                PluginManager.getLogger().info("Key Changed: " + e.getKeyChar());
                mH2Url = mTextField.getText();
                mModified = true;
            }
        });
        mainPaine.add(new JLabel("H2 Server Url:"));
        mainPaine.add(mTextField);
        // get content pane, which is usually the
        // Container of all the dialog's components.
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
        mStorage.setH2Url(mH2Url);
        mModified = false;
    }

    @Override
    public void reset() {
        PluginManager.getLogger().info("reset");
        if(!mStorage.getH2Url().isEmpty()) {
            mH2Url = mStorage.getH2Url();
        } else {
            mH2Url = defaultUrl;
        }
        mTextField.setText(mH2Url);
    }

    @Override
    public void disposeUIResources() {

    }
}