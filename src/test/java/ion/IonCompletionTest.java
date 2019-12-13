package ion;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;

public class IonCompletionTest extends LightPlatformCodeInsightFixtureTestCase {

  public void testLabel() {
    doTest("ok");
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data/completion";
  }

  public void doTest(String... expected) {
    String fileName = getTestName(true) + ".ion";
    PsiFile psiFile = myFixture.configureByFile(fileName);
    String text = psiFile.getText();
    String singleResolveToken = "/*!complete*/";
    if (text.contains(singleResolveToken)) {
      int offset = text.indexOf(singleResolveToken);
      myFixture.getEditor().getCaretModel().moveToOffset(offset + singleResolveToken.length());
      myFixture.completeBasic();
      List<String> lookupItems = myFixture.getLookupElementStrings();
      assertContainsElements(lookupItems, expected);
    } else {
      String initialCaretToken = "/*complete*/";
      int offset = text.indexOf(initialCaretToken);
      assertTrue(initialCaretToken + " is missing", offset != -1);
      while (offset != -1) {
        myFixture.getEditor().getCaretModel().moveToOffset(offset + initialCaretToken.length());
        myFixture.completeBasic();
        List<String> lookupItems = myFixture.getLookupElementStrings();
        assertContainsElements(lookupItems, expected);
        offset = text.indexOf(initialCaretToken, offset + 1);
      }
    }
  }
}
