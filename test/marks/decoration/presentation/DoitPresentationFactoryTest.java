package marks.decoration.presentation;

import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import static marks.decoration.presentation.DoitPresentationFactory.TODO;

public class DoitPresentationFactoryTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData";
    }

    public void testGetTodoCount_FiveTodos() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        int expectedTodoCount = 5;
        myFixture.configureByFiles("FiveMatches.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_TodoInMethodName() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("TodoInMethodName.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_TodoInStringLiteral() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("TodoInStringLiteral.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_MatchInImport() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("MatchInImport.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_MultipleInComment() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "- 1";
        myFixture.configureByFiles("MultipleInComment.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_TodoInClassName() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("TodoInClassName.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_CommentsWithNoTodos() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("CommentsWithNoTodos.java");
        PsiFile psiTestFile = myFixture.getFile();
        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }

    public void testGetTodoCount_ClassCommentaryClass() throws Exception {
        // GIVEN a test java class with expectedTodoCount todos
        String expectedTodoCount = "";
        myFixture.configureByFiles("ClassFileDecoration.java");
        PsiFile psiTestFile = myFixture.getFile();

        // WHEN getTodoCount is calculated.
        DoitPresentationFactory doitPresentationFactory = new DoitPresentationFactory();
        MarkPresentation markPresentation = doitPresentationFactory.createPresentation(psiTestFile, TODO);
        String markText = markPresentation.markText();

        // Then the result should be expectedTodoCount
        assertEquals(expectedTodoCount, markText);
    }
}