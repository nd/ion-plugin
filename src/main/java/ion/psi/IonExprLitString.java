package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.LeafElement;
import org.jetbrains.annotations.NotNull;

public class IonExprLitString extends IonExpr implements PsiLanguageInjectionHost {
  public IonExprLitString(@NotNull ASTNode node) {
    super(node);
  }

  public boolean isMultilineString() {
    return findChildByType(IonToken.MULTILINE_STRING) != null;
  }

  public int getQuoteLen() {
    return isMultilineString() ? 3 : 1;
  }

  @Override
  public boolean isValidHost() {
    return true;
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    ASTNode valueNode = this.getNode().getFirstChildNode();
    ((LeafElement)valueNode).replaceWithText(text);
    return this;
  }

  @Override
  @NotNull
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return new LiteralTextEscaper<IonExprLitString>(this) {
      @Override
      public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
        String subText = rangeInsideHost.substring(myHost.getText());
        outChars.append(subText);
        return true;
      }

      @Override
      public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
        return offsetInDecoded <= rangeInsideHost.getLength() ? offsetInDecoded + getQuoteLen() : -1;
      }

      @Override
      public boolean isOneLine() {
        return !myHost.isMultilineString();
      }

      @Override
      public @NotNull TextRange getRelevantTextRange() {
        int quoteLen = getQuoteLen();
        return TextRange.create(quoteLen, myHost.getTextLength() - quoteLen);
      }
    };
  }
}
