package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public class IonLabelName extends IonPsiElementBase {
  public IonLabelName(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return new IonReference(this, TextRange.create(0, getTextLength()));
  }
}
