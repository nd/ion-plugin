package ion;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.LexerTestCase;

import java.io.File;
import java.io.IOException;

public class LexerTest extends LexerTestCase {
  public void testPerf() throws IOException {
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
      assertEquals(10888, count);
    }
    System.out.println(System.currentTimeMillis() - t0); //17777
  }

  @Override
  protected Lexer createLexer() {
    return new IonLexer();
  }

  @Override
  protected String getDirPath() {
    return null;
  }
}
