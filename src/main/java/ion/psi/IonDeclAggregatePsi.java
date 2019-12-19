package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ObjectUtils;
import ion.psi.stub.IonDeclStubAggregate;
import org.jetbrains.annotations.NotNull;

public class IonDeclAggregatePsi extends IonStubBasedPsiElement<IonDeclStubAggregate> implements IonDeclAggregate {
  public IonDeclAggregatePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclAggregatePsi(@NotNull IonDeclStubAggregate stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclAggregate(aggregate)";
  }

  @Override
  @NotNull
  public Kind getKind() {
    IonDeclStubAggregate stub = getGreenStub();
    if (stub != null) {
      return stub.getKind();
    }
    PsiElement firstChild = ObjectUtils.notNull(getFirstChild());
    IElementType elementType = firstChild.getNode().getElementType();
    return elementType == IonToken.STRUCT ? Kind.STRUCT : Kind.UNION;
  }
}
