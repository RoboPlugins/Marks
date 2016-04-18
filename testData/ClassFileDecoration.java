/* 
 * $Id$
 */
package painpoint.decoration;

import com.intellij.ui.JBColor;
import painpoint.domain.util.ClassStatus;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.util.treeView.PresentableNodeDescriptor.ColoredFragment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.SimpleTextAttributes;

public class ClassFileDecoration {
    private final PainPointPresentation mPainPointsPresentation;

    public ClassFileDecoration(PainPointPresentation painPointsPresentation) {
        mPainPointsPresentation = painPointsPresentation;
    }

    protected String getName(ProjectViewNode node) {
        if (node instanceof ClassTreeNode) {
            ClassTreeNode classNode = (ClassTreeNode) node;
            return classNode.getPsiClass().getName();
        }
        return node.getName();
    }

    protected VirtualFile getVirtualFile(ProjectViewNode node) {
        ClassTreeNode classNode = (ClassTreeNode) node;
        return PsiUtilBase.getVirtualFile(classNode.getPsiClass());
    }

    public void decorate(ProjectViewNode node, PresentationData data) {
        addTodoStatusText(data, getName(node));
        addPainPointStatusText(data, getName(node));
    }

    private SimpleTextAttributes attributesByClassStatus(ClassStatus status) {
        return new SimpleTextAttributes(SimpleTextAttributes.STYLE_SMALLER,
                status.getColor());
    }

    protected void addPainPointStatusText(PresentationData data, String text) {
        boolean hasTodos = mPainPointsPresentation.hasTodos();

        if(hasTodos) {

            int todoCount = mPainPointsPresentation.getTodoCount();

            JBColor jbColor = JBColor.BLUE;

            ClassStatus status = mPainPointsPresentation.getClassStatus();
            SimpleTextAttributes textAttributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_SMALLER, jbColor);
            String statusLabel = " - " + todoCount;
            String finishText = text + " " + statusLabel;

            boolean add = true;
            boolean addOriginal = true;
            for (ColoredFragment existing : data.getColoredText()) {
                if (existing.getText().equals(finishText)) {
                    add = false;
                }
                if (existing.getText().contains(text)) {
                    addOriginal = false;
                }
            }
            if (add) {

                if(addOriginal) {
                    data.addText(text, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }
                data.addText(" " + statusLabel, textAttributes);
            }
        }
    }

    protected void addTodoStatusText(PresentationData data, String text) {

        boolean hasPainPoints = mPainPointsPresentation.hasPainPoints();
        if(hasPainPoints) {

            ClassStatus status = mPainPointsPresentation.getClassStatus();
            SimpleTextAttributes textAttributes = attributesByClassStatus(status);

            String statusLabel = " - " + mPainPointsPresentation.getPinnedCount();
            String finishText = text + " " + statusLabel;
            boolean add = true;
            boolean addOriginal = true;

            for (ColoredFragment existing : data.getColoredText()) {
                if (existing.getText().equals(finishText)) {
                    add = false;
                }
                if (existing.getText().contains(text)) {
                    addOriginal = false;
                }
            }
            if (add) {
                if(addOriginal) {
                    data.addText(text, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }
                data.addText(" " + statusLabel, textAttributes);
            }
        }
    }
}
