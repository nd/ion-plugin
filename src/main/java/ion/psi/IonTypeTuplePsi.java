package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubTuple;
import org.jetbrains.annotations.NotNull;

public class IonTypeTuplePsi extends IonStubBasedType<IonTypeStubTuple> implements IonTypeTuple {
  public IonTypeTuplePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeTuplePsi(@NotNull IonTypeStubTuple stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeTuple(type_tuple)";
  }
}
