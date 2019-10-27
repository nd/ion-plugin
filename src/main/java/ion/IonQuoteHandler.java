package ion;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.tree.IElementType;
import ion.psi.IonToken;

public class IonQuoteHandler extends SimpleTokenSetQuoteHandler {
  public IonQuoteHandler() {
    super(IonToken.STRING, IonToken.MULTILINE_STRING, IonToken.CHAR);
  }
}
