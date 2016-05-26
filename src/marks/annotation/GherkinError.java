package marks.annotation;

import com.intellij.psi.PsiElement;

/**
 * An error relating to adding Java Docs.
 */
public class GherkinError {
    private PsiElement elementToTag;
    private ErrorType errorType;

    /**
     * What kind of Gherkin error is this highlighting.
     */
    public enum ErrorType {
        MISSING_GHERKIN_GIVEN,
        MISSING_GHERKIN_WHEN,
        MISSING_GHERKIN_THEN
    }

    public GherkinError(PsiElement elementToTag, ErrorType errorType) {
        this.elementToTag = elementToTag;
        this.errorType = errorType;
    }

    /**
     * @return place to highlight the Gherkin error.
     */
    public PsiElement getElementToTag() {
        return elementToTag;
    }

    /**
     * @return message to be displayed to the user if this error happens.
     */
    public String messageForError() {
        switch (errorType) {
            case MISSING_GHERKIN_GIVEN:
                return "Missing GHERKIN - GIVEN";
            case MISSING_GHERKIN_WHEN:
                return "Missing GHERKIN - WHEN";
            case MISSING_GHERKIN_THEN:
                return "Missing GHERKIN - THEN";
            default:
                return "";
        }
    }
}
