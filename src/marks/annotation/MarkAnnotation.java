package marks.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkAnnotation implements Annotator {

    private static final String REGION = "region";
    private static final JBColor REGION_COLOR = new JBColor(8089544, 8089544);
    private static final EffectType REGION_DECORATION = EffectType.LINE_UNDERSCORE;
    private static final int REGION_TEXT_STYLE = Font.ITALIC;

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {

        final Project project = element.getProject();
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                FileEditorManager editorManager = FileEditorManager.getInstance(project);
                Editor editor = editorManager.getSelectedTextEditor();
                if (editor != null) {
                    FoldingModel foldingModel = editor.getFoldingModel();
                    FoldRegion[] foldRegions = foldingModel.getAllFoldRegions();
                    for (FoldRegion foldRegion : foldRegions) {
                        String placeholderText = foldRegion.getPlaceholderText();
                        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(placeholderText);
                        boolean b = m.find();
                        if (!b) {
                            highlightRegion(foldRegion, editor);
                        }
                    }
                }
            }
        });
    }

    private void highlightRegion(FoldRegion foldRegion, Editor editor) {

        TextAttributes textattributes = new TextAttributes(REGION_COLOR, null, REGION_COLOR, REGION_DECORATION, REGION_TEXT_STYLE);
        editor.getMarkupModel().addRangeHighlighter(
                foldRegion.getStartOffset(),
                foldRegion.getStartOffset() + foldRegion.getPlaceholderText().length(),
                HighlighterLayer.WARNING,
                textattributes,
                HighlighterTargetArea.LINES_IN_RANGE);

        editor.getMarkupModel().addRangeHighlighter(
                foldRegion.getEndOffset() - foldRegion.getPlaceholderText().length(),
                foldRegion.getEndOffset(),
                HighlighterLayer.WARNING,
                textattributes,
                HighlighterTargetArea.LINES_IN_RANGE);
    }
}
