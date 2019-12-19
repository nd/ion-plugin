package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonTypeStubName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonTypeNamePsi extends IonStubBasedType<IonTypeStubName> implements IonTypeName {
  public IonTypeNamePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonTypeNamePsi(@NotNull IonTypeStubName stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonTypeName(type_name)";
  }

  @Override
  public PsiReference getReference() {
    return new IonReference(this, TextRange.create(0, getTextLength()));
  }

  @Override
  @Nullable
  public String getName() {
    IonTypeStubName stub = getGreenStub();
    if (stub != null) {
      return stub.getName();
    }
    ASTNode nameNode = getNode().findChildByType(IonToken.NAME);
    return nameNode != null ? nameNode.getText() : null;
  }
}
