package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonNoteParam extends IonPsiElementBase {
  public IonNoteParam(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  public PsiElement getNoteParamName() {
    return findChildByType(IonToken.NAME);
  }
}
