package ion;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.LexerTestCase;
import ion.IonLexer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class IonLexerTest extends LexerTestCase {
  public void testInts() {
    doTest();
  }

  public void testFloats() {
    doTest();
  }

  public void testStrings() {
    doTest();
  }

  public void testChars() {
    doTest();
  }

  public void testExpressions() {
    doTest();
  }

  public void testKeywords() {
    doTest();
  }

  @Override
  protected Lexer createLexer() {
    return new IonLexer();
  }

  private void doTest() {
    doFileTest("ion");
  }

  @NotNull
  protected String getPathToTestDataFile(String extension) {
    return getDirPath() + "/" + getTestName(true) + extension;
  }

  @Override
  protected String getDirPath() {
    return "src/test/data/lexer";
  }
}
