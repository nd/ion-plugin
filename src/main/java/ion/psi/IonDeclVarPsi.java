package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import ion.psi.stub.IonDeclVarStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclVarPsi extends IonStubBasedPsiElement<IonDeclVarStub> implements IonDeclVar {
  public IonDeclVarPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclVarPsi(@NotNull IonDeclVarStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclVar(var)";
  }

  @Override
  public int getTextOffset() {
    PsiElement name = getNameIdentifier();
    return name != null ? name.getTextOffset() : super.getTextOffset();
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode nameNode = getNode().findChildByType(IonToken.NAME);
    return nameNode != null ? nameNode.getPsi() : null;
  }

  @Override
  public String getName() {
    IonDeclVarStub stub = getGreenStub();
    if (stub != null) {
      return stub.getName();
    }
    PsiElement name = getNameIdentifier();
    return name != null ? name.getText() : super.getName();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    PsiElement nameElement = getNameIdentifier();
    if (nameElement == null) {
      throw new IncorrectOperationException("Name element is not found");
    }
    if (nameElement instanceof LeafElement) {
      ((LeafElement) nameElement).replaceWithText(name);
      return this;
    } else {
      throw new IncorrectOperationException("Name element is not a leaf element");
    }
  }
}
