package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclBase extends IonPsiElementBase implements IonDecl {
  public IonDeclBase(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public int getTextOffset() {
    PsiElement name = getNameIdentifier();
    return name != null ? name.getTextOffset() : super.getTextOffset();
  }

  @Override
  public String getName() {
    PsiElement name = getNameIdentifier();
    return name != null ? name.getText() : super.getName();
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode nameNode = getNode().findChildByType(IonToken.NAME);
    return nameNode != null ? nameNode.getPsi() : null;
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
