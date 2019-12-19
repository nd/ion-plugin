package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubQName;
import org.jetbrains.annotations.NotNull;

public class IonTypeQNamePsi extends IonStubBasedType<IonTypeStubQName> implements IonTypeQName {
  public IonTypeQNamePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeQNamePsi(@NotNull IonTypeStubQName stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeQName(type_qname)";
  }
}
