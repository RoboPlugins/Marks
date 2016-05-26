package marks.common;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MarkParse {

    static final String TODO = "TODO";
    public static final String DOIT = "DOIT";
    public static final String GIVEN = "// GIVEN";
    public static final String WHEN = "// WHEN";
    public static final String THEN = "// THEN";

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
