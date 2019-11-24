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

  public void testFor() {
    doTest();
  }

  public void testIf() {
    doTest();
  }

  public void testElse() {
    doTest();
  }

  public void testFunc() {
    doTest();
  }

  public void testFuncParam() {
    doTest();
  }

  public void testFuncRecursive() {
    doTest();
  }

  public void testConst() {
    doTest();
  }

  public void testStruct() {
    doTest();
  }

  public void testUnion() {
    doTest();
  }

  public void testUnionField() {
    doTest();
  }

  public void testUnionFieldAnonymous() {
    doTest();
  }

  public void testUnionFieldAnonymousStruct() {
    doTest();
  }

  public void testTypedef() {
    doTest();
  }

  public void testLabel() {
    doTest();
  }

  public void testStructField() {
    doTest();
  }

  public void testStructFieldManyNames() {
    doTest();
  }

  public void testStructFieldChain() {
    doTest();
  }

  public void testStructFieldFuncParam() {
    doTest();
  }

  public void testStructFieldFuncCall() {
    doTest();
  }

  public void testStructFieldCompoundLiteral() {
    doTest();
  }

  public void testStructFieldFuncArg() {
    doTest();
  }

  public void testStructFieldInit() {
    doTest();
  }

  public void testStructFieldAssign() {
    doTest();
  }

  public void testStructFieldAssignChain() {
    doTest();
  }

  public void testArray() {
    doTest();
  }

  public void testEnum() {
    doTest();
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data/resolve";
  }

  public void doTest() {
    String fileName = getTestName(true) + ".ion";
    PsiFile psiFile = myFixture.configureByFile(fileName);
    String text = psiFile.getText();
    String singleResolveToken = "/*!resolve*/";
    if (text.contains(singleResolveToken)) {
      int offset = text.indexOf(singleResolveToken);
      myFixture.getEditor().getCaretModel().moveToOffset(offset + singleResolveToken.length());
      myFixture.performEditorAction(IdeActions.ACTION_GOTO_DECLARATION);
      myFixture.checkResultByFile(fileName);
    } else {
      String initialCaretToken = "/*resolve*/";
      int offset = text.indexOf(initialCaretToken);
      assertTrue(initialCaretToken + " is missing", offset != -1);
      while (offset != -1) {
        myFixture.getEditor().getCaretModel().moveToOffset(offset + initialCaretToken.length());
        myFixture.performEditorAction(IdeActions.ACTION_GOTO_DECLARATION);
        myFixture.checkResultByFile(fileName);
        offset = text.indexOf(initialCaretToken, offset + 1);
      }
    }
  }
}
