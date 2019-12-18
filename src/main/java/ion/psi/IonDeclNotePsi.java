package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import ion.psi.stub.IonDeclStubNote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclNotePsi extends IonStubBasedPsiElement<IonDeclStubNote> implements IonDeclNote {
  public IonDeclNotePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclNotePsi(@NotNull IonDeclStubNote stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclNote(note)";
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode lastChild = getNode().getLastChildNode();
    if (lastChild != null && lastChild.getElementType() == IonToken.RPAREN) {
      ASTNode prev = lastChild.getTreePrev();
      return prev != null && prev.getElementType() == IonToken.NAME ? prev.getPsi() : null;
    }
    return null;
  }
}
