package ion;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import ion.psi.IonDecl;
import ion.psi.IonElementType;
import ion.psi.IonPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonFindUsagesProvider implements FindUsagesProvider {
  @Override
  @Nullable
  public WordsScanner getWordsScanner() {
    return null;
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement element) {
    return element instanceof IonPsiElement;
  }

  @Override
  public @NotNull String getType(@NotNull PsiElement element) {
    IonElementType type = ObjectUtils.tryCast(element.getNode().getElementType(), IonElementType.class);
    IonElementType.TypeId typeId = type != null ? type.getTypeId() : null;
    if (typeId != null) {
      switch (typeId) {
        case DECL_AGGREGATE:
          PsiElement firstChild = element.getFirstChild();
          return firstChild != null ? firstChild.getText() : "aggregate";
        case DECL_VAR:
          return "var";
        case DECL_CONST:
          return "const";
        case DECL_ENUM:
          return "enum";
        case DECL_ENUM_ITEM:
          return "enum item";
        case DECL_FIELD:
        case DECL_FIELD_NAME:
          return "field";
        case DECL_FUNC:
          return "function";
        case DECL_FUNC_PARAM:
          return "function parameter";
        case DECL_TYPEDEF:
          return "typedef";
        case DECL_IMPORT:
          return "import";
        case DECL_IMPORT_ITEM:
          return "import item";
        case STMT_INIT:
          return "local";
        case STMT_LABEL:
          return "label";
      }
    }
    return "unknown";
  }

  @Override
  public @NotNull String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof IonDecl) {
      return ObjectUtils.notNull(((IonDecl) element).getName(), "");
    } else {
      return element.getText();
    }
  }

  @Override
  @NotNull
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof IonDecl) {
      return ObjectUtils.notNull(((IonDecl) element).getName(), "");
    } else {
      return element.getText();
    }
  }

  @Override
  @Nullable
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }
}
