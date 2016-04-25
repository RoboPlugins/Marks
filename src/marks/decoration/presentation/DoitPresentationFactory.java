package marks.decoration.presentation;

import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

import marks.common.MarkParse;

public class DoitPresentationFactory {

    public static final String TODO = "TODO";
    public static final String DOIT = "DOIT";

    public MarkPresentation createPresentation(PsiFile psiFile, String markType) {
        Integer markCount = checkMarkCache(psiFile, markType);
        if(markCount == null) {
            markCount = 0;
        }
        if(markType.equals(DOIT)) {
            return new DoitPresentation(markCount);
        }
        else {
            return new TodoPresentation(markCount);
        }
    }

    public MarkPresentation createPresentation(ClassTreeNode classTreeNode, String markType) {
        PsiFile psiFile = getClassNodePsiFile(classTreeNode);
        return createPresentation(psiFile, markType);
    }

    private Integer checkMarkCache(PsiFile psiFile, String markType) {
        Integer parsedCount = MarkParse.parseForMarks(psiFile).get(markType);
        return parsedCount;
    }

    private PsiFile getClassNodePsiFile(ClassTreeNode classTreeNode) {
        PsiClass psiClass = classTreeNode.getPsiClass();
        return psiClass.getContainingFile();
    }
}