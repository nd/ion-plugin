package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubEnum;
import org.jetbrains.annotations.NotNull;

public class IonDeclEnumPsi extends IonStubBasedPsiElement<IonDeclStubEnum> implements IonDeclEnum {
  public IonDeclEnumPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclEnumPsi(@NotNull IonDeclStubEnum stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclEnum(enum)";
  }
}
