package marks.decoration.presentation;

import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;

public class TodoPresentation implements MarkPresentation {

    private static final JBColor TEXT_COLOR = JBColor.BLUE;
    private static final int TEXT_SIZE = SimpleTextAttributes.STYLE_SMALLER;

    private int mTodoCount;

    TodoPresentation(int todoCount) {
        mTodoCount = todoCount;
    }

    @Override
    public String markText() {
        if(mTodoCount > 0 ) {
            return "- " + mTodoCount;
        }
        return "";
    }

    @Override
    public SimpleTextAttributes getTextAttributes() {
        return  new SimpleTextAttributes(TEXT_SIZE, TEXT_COLOR);
    }
}
