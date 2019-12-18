package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubField;
import org.jetbrains.annotations.NotNull;

public class IonDeclFieldPsi extends IonStubBasedPsiElement<IonDeclStubField> implements IonDeclField {
  public IonDeclFieldPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclFieldPsi(@NotNull IonDeclStubField stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclField(field)";
  }
}

