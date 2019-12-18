package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubConst;
import org.jetbrains.annotations.NotNull;

public class IonDeclConstPsi extends IonStubBasedPsiElement<IonDeclStubConst> implements IonDeclConst {
  public IonDeclConstPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclConstPsi(@NotNull IonDeclStubConst stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclConst(const)";
  }
}
