package ion;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;

public class IonCompletionTest extends BasePlatformTestCase {

  public void testLabel() {
    doTest("ok");
  }

  public void testStructField() {
    doTest("f1", "f2", "f3");
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data/completion";
  }

  private void doTest(String... expected) {
    String fileName = getTestName(true) + ".ion";
    myFixture.configureByFile(fileName);
    myFixture.completeBasic();
    List<String> lookupItems = myFixture.getLookupElementStrings();
    assertNotNull(lookupItems);
    assertContainsElements(lookupItems, expected);
  }
}
