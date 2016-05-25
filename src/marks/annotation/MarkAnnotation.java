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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.ui.JBColor;
import marks.common.MarkParse;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MarkAnnotation implements Annotator {

    //REGION
    private static final String REGION = "region";
    private static final JBColor REGION_COLOR = new JBColor(8089544, 8089544);
    private static final EffectType REGION_DECORATION = null;
    private static final int REGION_TEXT_STYLE = Font.BOLD;
    private static final int PLACEHOLDER_LENGTH_THRESHOLD = 20;
    private static final String DEV_REGION_IDENTIFIER = "---------------------";

    //DOIT
    private static final JBColor DOIT_COLOR = JBColor.ORANGE;
    private static final JBColor GERKIN_COLOR = JBColor.GREEN;
    private static final int HIGHLIGHT_TEXT_STYLE = Font.BOLD;


    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {


        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {

                Project project = element.getProject();
                FileEditorManager editorManager = FileEditorManager.getInstance(project);
                Editor editor = editorManager.getSelectedTextEditor();
                if (editor != null) {

                    //Hightlight Regions
                    FoldingModel foldingModel = editor.getFoldingModel();
                    FoldRegion[] foldRegions = foldingModel.getAllFoldRegions();
                    for (FoldRegion foldRegion : foldRegions) {
                        String placeholderText = foldRegion.getPlaceholderText();
                        if(placeholderText.length() >= PLACEHOLDER_LENGTH_THRESHOLD  &&
                                placeholderText.contains(DEV_REGION_IDENTIFIER)) {
                            highlightRegion(foldRegion, editor);
                        }
                    }

                    if(element instanceof PsiCommentImpl) {
                        if(StringUtils.containsIgnoreCase(element.getText(), MarkParse.DOIT)) {
                            //Highlight DOIT Marks.
                            highlightDOITElement(element, editor);
                        }
                        if( StringUtils.containsIgnoreCase(element.getText(), MarkParse.GIVEN) ||
                                StringUtils.containsIgnoreCase(element.getText(), MarkParse.WHEN) ||
                                StringUtils.containsIgnoreCase(element.getText(), MarkParse.THEN)) {
                            highlightTESTElement(element, editor);
                        }
                    }

                }
            }
        });
    }

    private static void highlightDOITElement(PsiElement psiElement, Editor editor) {

        TextRange textRange = psiElement.getTextRange();
        TextAttributes textattributes = new TextAttributes(DOIT_COLOR, null, DOIT_COLOR, null, HIGHLIGHT_TEXT_STYLE);
        editor.getMarkupModel().addRangeHighlighter(
                textRange.getStartOffset(),
                textRange.getStartOffset() + textRange.getLength(),
                HighlighterLayer.WARNING,
                textattributes,
                HighlighterTargetArea.LINES_IN_RANGE);
    }

    private static void highlightTESTElement(PsiElement psiElement, Editor editor) {

        TextRange textRange = psiElement.getTextRange();
        TextAttributes textattributes = new TextAttributes(GERKIN_COLOR, null, GERKIN_COLOR, null, HIGHLIGHT_TEXT_STYLE);
        editor.getMarkupModel().addRangeHighlighter(
                textRange.getStartOffset(),
                textRange.getStartOffset() + textRange.getLength(),
                HighlighterLayer.WARNING,
                textattributes,
                HighlighterTargetArea.EXACT_RANGE);
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
                foldRegion.getEndOffset() - 9, //9 for "endRegion"
                foldRegion.getEndOffset(),
                HighlighterLayer.WARNING,
                textattributes,
                HighlighterTargetArea.LINES_IN_RANGE);
    }
}
