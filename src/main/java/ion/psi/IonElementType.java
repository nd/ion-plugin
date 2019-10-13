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
  public static final IonElementType VAR_DECL = new IonElementType("var");
  public static final IonElementType EXPR_LITERAL_INT = new IonElementType("expr_literal_int");
  public static final IonElementType EXPR_LITERAL_FLOAT = new IonElementType("expr_literal_float");
  public static final IonElementType EXPR_LITERAL_STR = new IonElementType("expr_literal_str");
  public static final IonElementType EXPR_NAME = new IonElementType("expr_name");
  public static final IonElementType EXPR_NEW = new IonElementType("expr_new");
  public static final IonElementType EXPR_PAREN = new IonElementType("expr_paren");
  public static final IonElementType EXPR_POSTFIX = new IonElementType("expr_postfix");
  public static final IonElementType EXPR_INDEX = new IonElementType("expr_index");
  public static final IonElementType EXPR_FIELD = new IonElementType("expr_field");
  public static final IonElementType EXPR_CALL = new IonElementType("expr_call");
  public static final IonElementType EXPR_UNARY = new IonElementType("expr_unary");
  public static final IonElementType EXPR_BINARY = new IonElementType("expr_binary");
  public static final IonElementType EXPR_TERNARY = new IonElementType("expr_ternary");

  public IonElementType(@NotNull String debugName) {
    super(debugName, IonLanguage.INSTANCE);
  }

  @NotNull
  public PsiElement createPsiElement(ASTNode node) {
    return new IonPsiElement(node);
  }
}
