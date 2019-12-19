package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.stub.IonDeclStubFunc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclFuncPsi extends IonStubBasedPsiElement<IonDeclStubFunc> implements IonDeclFunc {
  public IonDeclFuncPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclFuncPsi(@NotNull IonDeclStubFunc stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclFunc(func)";
  }

  @Override
  @Nullable
  public PsiElement getType() {
    return PsiTreeUtil.getStubChildOfType(this, IonType.class);
  }
}
