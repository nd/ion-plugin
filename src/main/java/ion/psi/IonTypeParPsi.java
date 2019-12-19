package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubPar;
import org.jetbrains.annotations.NotNull;

public class IonTypeParPsi extends IonStubBasedType<IonTypeStubPar> implements IonTypePar {
  public IonTypeParPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeParPsi(@NotNull IonTypeStubPar stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypePar(type_par)";
  }
}
