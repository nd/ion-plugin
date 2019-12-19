package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.stub.IonDeclStubFuncParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclFuncParamPsi extends IonStubBasedPsiElement<IonDeclStubFuncParam> implements IonDeclFuncParam {
  public IonDeclFuncParamPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclFuncParamPsi(@NotNull IonDeclStubFuncParam stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclFuncParam(param)";
  }

  @Override
  public @Nullable PsiElement getType() {
    return PsiTreeUtil.getStubChildOfType(this, IonType.class);
  }
}
