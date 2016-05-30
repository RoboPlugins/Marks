package marks.annotation;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.ui.JBColor;
import marks.common.MarkParse;
import marks.config.Configuration;
import marks.config.Storage;
import marks.git.GitRunner;
import marks.pairing.PairConfig;
import marks.pairing.PairController;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MarkAnnotation implements Annotator {

    //REGION
    private static final Key HIGHLIGHTED_KEY = Key.create("h");
    private static final String HIGHLIGHTED_VALUE = "a";
    private Color mRegionColor = new JBColor(8089544, 8089544);
    private static final EffectType REGION_DECORATION = null;
    private static final int REGION_TEXT_STYLE = Font.BOLD;
    private static final int PLACEHOLDER_LENGTH_THRESHOLD = 20;
    private static final String DEV_REGION_IDENTIFIER = "---------------------";

    //DOIT
    private static final JBColor DOIT_COLOR = JBColor.ORANGE;
    private static final JBColor GERKIN_COLOR = JBColor.GREEN;
    private static final int HIGHLIGHT_TEXT_STYLE = Font.BOLD;


    private String getPairs(Project project) {

        String projectPath = project.getBasePath();
        if (projectPath == null) {
            return null;
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
            return null;
        }

        PairConfig pairConfig = new PairConfig(configYaml);
        GitRunner gitRunner = new GitRunner(projectPath);
        PairController pairController = new PairController(pairConfig, gitRunner);
        pairController.init();
        return pairController.getPairDisplayName();
    }

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {

//        Project project = element.getProject();
//        String pairs = getPairs(project);
//        if (pairs != null && pairs.contains("Kennedy")) {
//            mRegionColor = JBColor.PINK;
//        }
        Storage storage = ServiceManager.getService(Storage.class);
        Configuration configuration = new Configuration();
        JBColor storedColor = configuration.getRegionColor();
        Integer regionColorRGB = storage.getRegionColor();
        if(storedColor != null) {
            mRegionColor = storedColor;
        }
        //Hightlight by Regions
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                if (element instanceof PsiCommentImpl) {
                    Editor editor = getEditor(element);
                    if (editor != null) {
                        FoldingModel foldingModel = editor.getFoldingModel();
                        FoldRegion[] foldRegions = foldingModel.getAllFoldRegions();
                        for (FoldRegion foldRegion : foldRegions) {
                            if (foldRegion.getUserData(HIGHLIGHTED_KEY) == null) {
                                String placeholderText = foldRegion.getPlaceholderText();
                                if (placeholderText.length() >= PLACEHOLDER_LENGTH_THRESHOLD &&
                                        placeholderText.contains(DEV_REGION_IDENTIFIER)) {
                                    highlightRegion(foldRegion, editor);
                                    foldRegion.putUserData(HIGHLIGHTED_KEY, HIGHLIGHTED_VALUE);
                                }
                            }
                        }
                    }
                }
            }
        });

        //Highlight by Keywords
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                if (element instanceof PsiCommentImpl) {
                    Editor editor = getEditor(element);
                    if (editor != null) {
                        // Highlight DOIT comments.
                        if (StringUtils.containsIgnoreCase(element.getText(), MarkParse.DOIT)) {
                            //Highlight DOIT Marks.
                            if (element.getUserData(HIGHLIGHTED_KEY) == null) {
                                highlightDOITElement(element, editor);
                                element.putUserData(HIGHLIGHTED_KEY, HIGHLIGHTED_VALUE);
                            }
                        }
                        // Highlight GERKIN comments.
                        else if (StringUtils.contains(element.getText(), MarkParse.GIVEN) ||
                                StringUtils.contains(element.getText(), MarkParse.WHEN) ||
                                StringUtils.contains(element.getText(), MarkParse.THEN)) {
                            if (element.getUserData(HIGHLIGHTED_KEY) == null) {
                                highlightTESTElement(element, editor);
                                element.putUserData(HIGHLIGHTED_KEY, HIGHLIGHTED_VALUE);
                            }
                        }
                    }
                }
            }
        });

        // Parse for Missing GHERKIN in tests.  Only in test methods and test classes.
        if (element instanceof PsiMethodImpl) {
            PsiMethodImpl psiMethod = (PsiMethodImpl) element;
            PsiClass psiClass = psiMethod.getContainingClass();
            if (psiClass != null) {
                String className = psiClass.getName();
                if (className != null && className.endsWith("Test")) {
                    GherkinParser gherkinParser = new GherkinParser();
                    for (GherkinError error : gherkinParser.parse(element)) {
                        holder.createWarningAnnotation(error.getElementToTag(), error.messageForError());
                    }
                }
            }
        }
    }

    @Nullable
    private Editor getEditor(final PsiElement element) {
        Project project = element.getProject();
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        return editorManager.getSelectedTextEditor();
    }

    private static void highlightDOITElement(PsiElement psiElement, Editor editor) {

        try {
            TextRange textRange = psiElement.getTextRange();
            TextAttributes textattributes = new TextAttributes(DOIT_COLOR, null, DOIT_COLOR, null, HIGHLIGHT_TEXT_STYLE);
            editor.getMarkupModel().addRangeHighlighter(
                    textRange.getStartOffset(),
                    textRange.getStartOffset() + textRange.getLength(),
                    HighlighterLayer.WARNING,
                    textattributes,
                    HighlighterTargetArea.LINES_IN_RANGE);
        } catch (IllegalArgumentException ex) {
            PluginManager.getLogger().warn("IllegalArgumentException: " + ex.getMessage());
        }
    }

    private static void highlightTESTElement(PsiElement psiElement, Editor editor) {

        try {
            TextRange textRange = psiElement.getTextRange();
            TextAttributes textattributes = new TextAttributes(GERKIN_COLOR, null, GERKIN_COLOR, null, HIGHLIGHT_TEXT_STYLE);
            editor.getMarkupModel().addRangeHighlighter(
                    textRange.getStartOffset(),
                    textRange.getStartOffset() + textRange.getLength(),
                    HighlighterLayer.WARNING,
                    textattributes,
                    HighlighterTargetArea.EXACT_RANGE);
        } catch (IllegalArgumentException ex) {
            PluginManager.getLogger().warn("IllegalArgumentException: " + ex.getMessage());
        }
    }

    private void highlightRegion(FoldRegion foldRegion, Editor editor) {

        try {
            TextAttributes textattributes = new TextAttributes(mRegionColor, null, mRegionColor, REGION_DECORATION, REGION_TEXT_STYLE);
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
        } catch (IllegalArgumentException ex) {
            PluginManager.getLogger().warn("IllegalArgumentException: " + ex.getMessage());
        }
    }
}
