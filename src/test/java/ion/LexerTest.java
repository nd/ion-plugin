package ion;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.LexerTestCase;
import ion.IonLexer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LexerTest extends LexerTestCase {
  public void testInts() {
    doTest();
  }

  public void testFloats() {
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

  public void _testPerf() throws IOException {
    String text = new String(FileUtil.loadFileText(new File("/home/nd/p/bitwise/ion/test1/test1.ion")));
    Lexer lexer = createLexer();
    long t0 = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      lexer.start(text, 0, text.length());
      int count = 0;
      while (lexer.getTokenType() != null) {
        lexer.advance();
        count++;
      }
      assertEquals(10010, count);
    }
    System.out.println(System.currentTimeMillis() - t0); //16887
  }
}
