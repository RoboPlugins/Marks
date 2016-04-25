package marks.common;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class MarkParse {

    static final String TODO = "TODO";
    static final String DOIT = "DOIT";

    public static Map<String, Integer> parseForMarks(PsiFile psiFile) {
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
