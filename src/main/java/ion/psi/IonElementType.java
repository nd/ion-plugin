package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonElementType extends IElementType {
  public static final IonElementType IMPORT_DECL = new IonElementType("import");
  public static final IonElementType IMPORT_ITEM = new IonElementType("import_item");

  public IonElementType(@NotNull String debugName) {
    super(debugName, IonLanguage.INSTANCE);
  }

  @NotNull
  public PsiElement createPsiElement(ASTNode node) {
    return new IonPsiElement(node);
  }
}
