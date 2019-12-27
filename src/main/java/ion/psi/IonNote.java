package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonNote extends IonPsiElementBase {
  public IonNote(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  public PsiElement getNoteName() {
    return findChildByClass(IonExprName.class); //todo support qnames
  }
}
