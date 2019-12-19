package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubPtr;
import org.jetbrains.annotations.NotNull;

public class IonTypePtrPsi extends IonStubBasedType<IonTypeStubPtr> implements IonTypePtr {
  public IonTypePtrPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypePtrPsi(@NotNull IonTypeStubPtr stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypePtr(type_ptr)";
  }
}
