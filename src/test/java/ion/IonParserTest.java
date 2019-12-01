package ion;

import com.intellij.lang.ParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.annotations.NotNull;

public class IonParserTest extends ParsingTestCase {
  public IonParserTest() {
    super("parser", "ion", new IonParserDefinition());
  }

  public void testImports() {
    doTest();
  }

  public void testVars() {
    doTest();
  }

  public void testConsts() {
    doTest();
  }

  public void testExpressions() {
    doTest();
  }

  public void testTypes() {
    doTest();
  }

  public void testEnums() {
    doTest();
  }

  public void testAggregates() {
    doTest();
  }

  public void testTypedefs() {
    doTest();
  }

  public void testFuncs() {
    doTest();
  }

  public void testNotes() {
    doTest();
  }

  public void testStmts() {
    doTest();
  }

  public void testErrorTypes() {
    doTest();
  }

  public void testErrorDecls() {
    doTest();
  }

  private void doTest() {
    doTest(true);
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/data";
  }

  @NotNull
  @Override
  protected String getTestName(boolean lowercaseFirstLetter) {
    return super.getTestName(true);
  }
}
