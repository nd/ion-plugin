package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import ion.psi.stub.IonDeclStubAggregate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
