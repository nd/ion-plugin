package ion.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class IonPsiElementBase extends ASTWrapperPsiElement implements IonPsiElement {
  public IonPsiElementBase(@NotNull ASTNode node) {
    super(node);
  }
}
