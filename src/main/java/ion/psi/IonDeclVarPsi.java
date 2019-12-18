package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubVar;
import org.jetbrains.annotations.NotNull;

public class IonDeclVarPsi extends IonStubBasedPsiElement<IonDeclStubVar> implements IonDeclVar {
  public IonDeclVarPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclVarPsi(@NotNull IonDeclStubVar stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclVar(var)";
  }
}
