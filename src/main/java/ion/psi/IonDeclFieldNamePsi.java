package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubFieldName;
import org.jetbrains.annotations.NotNull;

public class IonDeclFieldNamePsi extends IonStubBasedPsiElement<IonDeclStubFieldName> implements IonDeclFieldName {
  public IonDeclFieldNamePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclFieldNamePsi(@NotNull IonDeclStubFieldName stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclFieldName(field_name)";
  }
}
