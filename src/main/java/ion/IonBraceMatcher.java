package ion;

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter;
import com.intellij.lang.BracePair;
import com.intellij.lang.Language;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonBraceMatcher implements PairedBraceMatcher {
  private final BracePair[] PAIRS = new BracePair[]{
          new BracePair(IonToken.LPAREN, IonToken.RPAREN, false),
          new BracePair(IonToken.LBRACE, IonToken.RBRACE, true),
          new BracePair(IonToken.LBRACKET, IonToken.RBRACKET, false)
  };

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
