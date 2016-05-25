/* 
 * @(#) $Id:  $
 */
package marks.decoration;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.ui.ColoredTreeCellRenderer;
import marks.decoration.decoration.MarkDecoration;
import marks.decoration.presentation.*;

public class MarkProjectViewNodeDecorator implements ProjectViewNodeDecorator {

    //Test TODO
    //Test DOIT

    @Override
    public void decorate(ProjectViewNode viewNode, PresentationData presentationData) {
        if (viewNode != null && viewNode instanceof ClassTreeNode) {
            final ClassTreeNode classTreeNode = (ClassTreeNode) viewNode;
            DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
            DoitPresentation presentation = (DoitPresentation)doitPresentationFactory.createPresentation(classTreeNode, DoitPresentationFactory.DOIT);
            TodoPresentation todoPresentation = (TodoPresentation)doitPresentationFactory.createPresentation(classTreeNode, DoitPresentationFactory.TODO);

            MarkDecoration markDecoration = new MarkDecoration(presentation);
            MarkDecoration todoDecoration = new MarkDecoration(todoPresentation);

            todoDecoration.decorate(classTreeNode, presentationData);
            markDecoration.decorate(classTreeNode, presentationData);
        }
    }

    @Override
    public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {
        PluginManager.getLogger().warn("Decorate package dependencies");
    }
}
