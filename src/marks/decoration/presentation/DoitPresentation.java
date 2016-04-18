package marks.decoration.presentation;

import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;

public class DoitPresentation implements MarkPresentation {

    private static final JBColor TEXT_COLOR = JBColor.ORANGE;
    private static final int TEXT_SIZE = SimpleTextAttributes.STYLE_SMALLER;

    private int mMarkCount;

    DoitPresentation(int markCount) {
        mMarkCount = markCount;
    }

    @Override
    public String markText() {
        if(mMarkCount > 0 ) {
            return "- " + mMarkCount;
        }
        return "";
    }

    @Override
    public SimpleTextAttributes getTextAttributes() {
        return  new SimpleTextAttributes(TEXT_SIZE, TEXT_COLOR);
    }


}
