package ion;

import com.intellij.lang.ParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import org.jetbrains.annotations.NotNull;

public class IonParserTest extends ParsingTestCase {
  public IonParserTest() {
    super("parser", "ion", new IonParserDefinition());
  }

  public void testImport() {
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
