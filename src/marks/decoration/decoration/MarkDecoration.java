/*
 * $Id$
 */
package marks.decoration.decoration;

import marks.decoration.presentation.MarkPresentation;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.util.treeView.PresentableNodeDescriptor.ColoredFragment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.SimpleTextAttributes;

public class MarkDecoration {

    private final MarkPresentation mMarkPresentation;

    public MarkDecoration(MarkPresentation markPresentation) {
        mMarkPresentation = markPresentation;
    }

    protected VirtualFile getVirtualFile(ProjectViewNode node) {
        ClassTreeNode classNode = (ClassTreeNode) node;
        return PsiUtilBase.getVirtualFile(classNode.getPsiClass());
    }

    private String getName(ProjectViewNode node) {
        if (node instanceof ClassTreeNode) {
            ClassTreeNode classNode = (ClassTreeNode) node;
            return classNode.getPsiClass().getName();
        }
        return node.getName();
    }

    public void decorate(ProjectViewNode node, PresentationData data) {
        addMark(data, getName(node));
    }

    private void addMark(PresentationData data, String text) {

        String markText = mMarkPresentation.markText();
        if(markText.isEmpty()) {
            return;
        }
        SimpleTextAttributes textAttributes = mMarkPresentation.getTextAttributes();

        String finishText = text + " " + markText;

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

            if (addOriginal) {
                data.addText(text, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
            data.addText(" " + markText, textAttributes);
        }
    }
}
