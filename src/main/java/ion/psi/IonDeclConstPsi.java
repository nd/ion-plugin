package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import ion.psi.stub.IonDeclStubConst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclConstPsi extends IonStubBasedPsiElement<IonDeclStubConst> implements IonDeclConst {
  public IonDeclConstPsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclConstPsi(@NotNull IonDeclStubConst stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclConst(const)";
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
    IonDeclStubConst stub = getGreenStub();
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
