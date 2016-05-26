package marks.annotation;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiMethodImpl;
import marks.common.MarkParse;

import java.util.ArrayList;
import java.util.List;

class GherkinParser {

    /**
     * Get any Java Doc errors from the element.
     *
     * @param psiElement check for gherkin errors.
     * @return list of errors to mark.
     */
    public List<GherkinError> parse(PsiElement psiElement) {
        ArrayList<GherkinError> errors = new ArrayList<GherkinError>();
        if (psiElement instanceof PsiMethodImpl) {
            PsiMethodImpl psiMethod = (PsiMethodImpl) psiElement;
            String methodName = psiMethod.getName();
            PsiElement methodNameElement = psiMethod.getNameIdentifier();
            if (methodName.startsWith("test")) {
                if(!hasGherkin(psiMethod, MarkParse.GIVEN)) {
                    errors.add(new GherkinError(methodNameElement, GherkinError.ErrorType.MISSING_GHERKIN_GIVEN));
                }
                if(!hasGherkin(psiMethod, MarkParse.WHEN)) {
                    errors.add(new GherkinError(methodNameElement, GherkinError.ErrorType.MISSING_GHERKIN_WHEN));
                }
                if(!hasGherkin(psiMethod, MarkParse.THEN)) {
                    errors.add(new GherkinError(methodNameElement, GherkinError.ErrorType.MISSING_GHERKIN_THEN));
                }
            }
        }
        return errors;
    }

    /**
     * Determines whether or not a method body has the required GHERKIN.
     *
     * @param psiMethod the method to test.
     * @return true if the given method body has the required GHERKIN.
     */
    private boolean hasGherkin(final PsiMethodImpl psiMethod, final String gherkin) {
            PsiCodeBlock methodCodeBlock = psiMethod.getBody();
            if (methodCodeBlock != null) {
                String methodBody = methodCodeBlock.getText();
                if (methodBody.contains(gherkin)) {
                    return true;
                } else {
                    return false;
                }
            }
        return false;
    }
}
