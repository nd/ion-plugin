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
  public static final IonElementType CONST_DECL = new IonElementType("const");
  public static final IonElementType EXPR_LITERAL_INT = new IonElementType("expr_literal_int");
  public static final IonElementType EXPR_LITERAL_FLOAT = new IonElementType("expr_literal_float");
  public static final IonElementType EXPR_LITERAL_STR = new IonElementType("expr_literal_str");
  public static final IonElementType EXPR_LITERAL_COMPOUND = new IonElementType("expr_literal_compound");
  public static final IonElementType EXPR_LITERAL_COMPOUND_TYPED = new IonElementType("expr_literal_compound_typed");
  public static final IonElementType COMPOUND_FIELD = new IonElementType("compound_field");
  public static final IonElementType COMPOUND_FIELD_NAMED = new IonElementType("compound_field_named");
  public static final IonElementType COMPOUND_FIELD_INDEX = new IonElementType("compound_field_index");
  public static final IonElementType EXPR_NAME = new IonElementType("expr_name");
  public static final IonElementType EXPR_NEW = new IonElementType("expr_new");
  public static final IonElementType EXPR_PAREN = new IonElementType("expr_paren");
  public static final IonElementType EXPR_CAST = new IonElementType("expr_cast");
  public static final IonElementType EXPR_POSTFIX = new IonElementType("expr_postfix");
  public static final IonElementType EXPR_INDEX = new IonElementType("expr_index");
  public static final IonElementType EXPR_FIELD = new IonElementType("expr_field");
  public static final IonElementType EXPR_CALL = new IonElementType("expr_call");
  public static final IonElementType EXPR_UNARY = new IonElementType("expr_unary");
  public static final IonElementType EXPR_BINARY = new IonElementType("expr_binary");
  public static final IonElementType EXPR_TERNARY = new IonElementType("expr_ternary");
  public static final IonElementType TYPE = new IonElementType("type");
  public static final IonElementType TYPE_PTR = new IonElementType("type_ptr");
  public static final IonElementType TYPE_CONST = new IonElementType("type_const");
  public static final IonElementType TYPE_ARRAY = new IonElementType("type_array");
  public static final IonElementType TYPE_TUPLE = new IonElementType("type_tuple");
  public static final IonElementType TYPE_FUNC = new IonElementType("type_func");
  public static final IonElementType TYPE_FUNC_PARAM = new IonElementType("type_func_param");

  public IonElementType(@NotNull String debugName) {
    super(debugName, IonLanguage.INSTANCE);
  }

  @NotNull
  public PsiElement createPsiElement(ASTNode node) {
    return new IonPsiElement(node);
  }
}
