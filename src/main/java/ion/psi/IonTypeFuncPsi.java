package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubFunc;
import org.jetbrains.annotations.NotNull;

public class IonTypeFuncPsi extends IonStubBasedType<IonTypeStubFunc> implements IonTypeFunc {
  public IonTypeFuncPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeFuncPsi(@NotNull IonTypeStubFunc stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeFunc(type_func)";
  }
}
