package ion;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.psi.PsiFile;
import com.intellij.rt.execution.junit.FileComparisonFailure;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

public class IonResolveTest extends LightPlatformCodeInsightFixtureTestCase {
  public void testLocalVarSameBlock() {
    doTest();
  }

  public void testLocalVarDeclaredAfter() {
    doTest();
  }

  public void testGlobalVar() {
    doTest();
  }

  public void testGlobalVarDeclaredAfter() {
    doTest();
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data/resolve";
  }

  public void doTest() {
    String fileName = getTestName(true) + ".ion";
    PsiFile psiFile = myFixture.configureByFile(fileName);
    String initialCaretToken = "/*start*/";
    int initialOffset = psiFile.getText().indexOf(initialCaretToken);
    assertTrue(initialCaretToken + " is missing", initialOffset != -1);
    myFixture.getEditor().getCaretModel().moveToOffset(initialOffset + initialCaretToken.length());
    myFixture.performEditorAction(IdeActions.ACTION_GOTO_DECLARATION);
    myFixture.checkResultByFile(fileName);
  }
}
