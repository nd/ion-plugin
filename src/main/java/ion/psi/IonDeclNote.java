package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclNote extends IonDeclBase {
  public IonDeclNote(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode lastChild = getNode().getLastChildNode();
    if (lastChild != null && lastChild.getElementType() == IonToken.RPAREN) {
      ASTNode prev = lastChild.getTreePrev();
      return prev != null && prev.getElementType() == IonToken.NAME ? prev.getPsi() : null;
    }
    return null;
  }
}
