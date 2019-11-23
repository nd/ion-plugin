package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public class IonExprField extends IonExpr {
  public IonExprField(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    PsiElement fieldName = IonReference.getFieldName(this);
    return new IonReference(this, TextRange.from(fieldName.getStartOffsetInParent(), fieldName.getTextLength()));
  }
}
