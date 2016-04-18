package marks.component;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import marks.git.GitRunner;
import marks.pairing.PairConfig;
import marks.pairing.PairController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProjectViewManager extends AbstractProjectComponent {

    private MessageBusConnection mConnection;
    private PairController mPairController;

    public ProjectViewManager(Project project) {
        super(project);
        updateState(project);
    }

    public static ProjectViewManager getInstance(Project project) {
        return project.getComponent(ProjectViewManager.class);
    }

    @Override
    public void initComponent() {
        super.initComponent();
    }

    /**
     * Ask git who is the current user, and update our internal state.
     */
    private boolean updateState(Project project) {
        if (project == null) {
            return false;
        }

        String projectPath = project.getBasePath();
        if (projectPath == null) {
            return false;
        }

        String configFile = projectPath.concat("/.pairs");
        String configYaml = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = br.readLine()) != null) {
                configYaml += line + "\n";
            }
        } catch (IOException e) {
            System.out.println("Git Pair plugin couldn't open " + configFile + ": " + e.getMessage());
            return false;
        }
        PairConfig pairConfig = new PairConfig(configYaml);
        GitRunner gitRunner = new GitRunner(projectPath);
        mPairController = new PairController(pairConfig, gitRunner);
        mPairController.init();

        return true;
    }
}
