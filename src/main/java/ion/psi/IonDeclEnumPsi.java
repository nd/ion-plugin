package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.stub.IonDeclStubEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclEnumPsi extends IonStubBasedPsiElement<IonDeclStubEnum> implements IonDeclEnum {
  public IonDeclEnumPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclEnumPsi(@NotNull IonDeclStubEnum stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclEnum(enum)";
  }

  @Override
  @Nullable
  public PsiElement getType() {
    return PsiTreeUtil.getStubChildOfType(this, IonType.class);
  }
}
