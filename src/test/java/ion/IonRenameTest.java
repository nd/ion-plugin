package ion;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class IonRenameTest extends BasePlatformTestCase {

  public void testVar() {
    doTest();
  }

  public void testField() {
    doTest();
  }

  public void testFunc() {
    doTest();
  }

  public void testStruct() {
    doTest();
  }

  public void testTypedef() {
    doTest();
  }

  public void testLabel() {
    doTest();
  }

  public void testImportAlias() {
    doTest();
  }

  public void testImportNameAlias() {
    doTest();
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data/rename";
  }

  private void doTest() {
    String testName = getTestName(true);
    String fileName = testName + ".ion";
    myFixture.configureByFile(fileName);
    myFixture.renameElementAtCaret("y");
    myFixture.checkResultByFile(testName + "-after.ion");
  }
}
