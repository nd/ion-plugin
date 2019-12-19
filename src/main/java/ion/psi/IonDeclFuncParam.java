package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclFuncParam extends IonDeclBase implements IonTypeOwner {
  public IonDeclFuncParam(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  @Nullable
  public PsiElement getType() {
    return PsiTreeUtil.getStubChildOfType(this, IonType.class);
  }
}
