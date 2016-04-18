package marks.decoration.presentation;

import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        Integer parsedCount = parseForMarks(psiFile).get(markType);
        return parsedCount;
    }

    private PsiFile getClassNodePsiFile(ClassTreeNode classTreeNode) {
        PsiClass psiClass = classTreeNode.getPsiClass();
        return psiClass.getContainingFile();
    }

    private Map<String, Integer> parseForMarks(PsiFile psiFile) {
        final Map<String, Integer> marks = new HashMap<String, Integer>();
        marks.put(TODO, 0);
        marks.put(DOIT, 0);
        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);

                if (element instanceof PsiCommentImpl) {
                    String commentText = element.getText();
                    if (StringUtils.containsIgnoreCase(commentText, TODO)) {
                        marks.put(TODO, marks.get(TODO) + 1);
                    }
                    if (StringUtils.containsIgnoreCase(commentText, DOIT)) {
                        marks.put(DOIT, marks.get(DOIT) + 1);
                    }
                }
            }
        });

        return marks;
    }

}