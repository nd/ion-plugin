package ion.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class IonPsiElement extends ASTWrapperPsiElement implements PsiElement {
  public IonPsiElement(@NotNull ASTNode node) {
    super(node);
  }
}
