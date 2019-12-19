package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubArray;
import org.jetbrains.annotations.NotNull;

public class IonTypeArrayPsi extends IonStubBasedType<IonTypeStubArray> implements IonTypeArray {
  public IonTypeArrayPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeArrayPsi(@NotNull IonTypeStubArray stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeArray(type_array)";
  }
}

