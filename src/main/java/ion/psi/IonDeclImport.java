package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclImport extends IonDecl {
  public IonDeclImport(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode nameNode = getNode().findChildByType(IonToken.NAME);
    if (nameNode != null) {
      return nameNode.getPsi();
    } else {
      IonImportPath importPath = PsiTreeUtil.getChildOfType(this, IonImportPath.class);
      nameNode = importPath != null ? importPath.getNode().findChildByType(IonToken.NAME) : null;
      return nameNode != null ? nameNode.getPsi() : null;
    }
  }
}
