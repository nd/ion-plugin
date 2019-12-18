package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubEnumItem;
import org.jetbrains.annotations.NotNull;

public class IonDeclEnumItemPsi extends IonStubBasedPsiElement<IonDeclStubEnumItem> implements IonDeclEnumItem {
  public IonDeclEnumItemPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclEnumItemPsi(@NotNull IonDeclStubEnumItem stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclEnumItem(enum_item)";
  }
}
