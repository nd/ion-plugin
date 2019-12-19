package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.stub.IonDeclStubTypedef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclTypedefPsi extends IonStubBasedPsiElement<IonDeclStubTypedef> implements IonDeclTypedef {
  public IonDeclTypedefPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclTypedefPsi(@NotNull IonDeclStubTypedef stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclTypedef(typedef)";
  }

  @Override
  @Nullable
  public PsiElement getType() {
    return PsiTreeUtil.getStubChildOfType(this, IonType.class);
  }
}
