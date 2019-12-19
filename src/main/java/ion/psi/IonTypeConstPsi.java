package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubConst;
import org.jetbrains.annotations.NotNull;

public class IonTypeConstPsi extends IonStubBasedType<IonTypeStubConst> implements IonTypeConst {
  public IonTypeConstPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeConstPsi(@NotNull IonTypeStubConst stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeConst(type_const)";
  }
}
