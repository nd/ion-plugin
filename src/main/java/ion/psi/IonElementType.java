package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonElementType extends IElementType implements IonElementTypeIdOwner {
  public static final IonElementTypeConst DECL_CONST = new IonElementTypeConst("const", TypeId.DECL_CONST);
  public static final IonElementTypeVar DECL_VAR = new IonElementTypeVar("var", TypeId.DECL_VAR);
  public static final IonElementTypeEnum DECL_ENUM = new IonElementTypeEnum("enum", TypeId.DECL_ENUM);
  public static final IonElementTypeEnumItem DECL_ENUM_ITEM = new IonElementTypeEnumItem("enum_item", TypeId.DECL_ENUM_ITEM);
  public static final IonElementTypeAggregate DECL_AGGREGATE = new IonElementTypeAggregate("aggregate", TypeId.DECL_AGGREGATE);
  public static final IonElementTypeField DECL_FIELD = new IonElementTypeField("field", TypeId.DECL_FIELD);
  public static final IonElementTypeFieldName DECL_FIELD_NAME = new IonElementTypeFieldName("field_name", TypeId.DECL_FIELD_NAME);
  public static final IonElementType DECL_IMPORT = new IonElementType("import", TypeId.DECL_IMPORT);
  public static final IonElementTypeTypedef DECL_TYPEDEF = new IonElementTypeTypedef("typedef", TypeId.DECL_TYPEDEF);
  public static final IonElementTypeFunc DECL_FUNC = new IonElementTypeFunc("func", TypeId.DECL_FUNC);
  public static final IonElementTypeFuncParam DECL_FUNC_PARAM = new IonElementTypeFuncParam("param", TypeId.DECL_FUNC_PARAM);
  public static final IonElementTypeNote DECL_NOTE = new IonElementTypeNote("decl_note", TypeId.DECL_NOTE);
  public static final IonElementType NOTE = new IonElementType("note");
  public static final IonElementType NOTE_PARAM = new IonElementType("note_param");
  public static final IonElementType DECL_IMPORT_ITEM = new IonElementType("import_item", TypeId.DECL_IMPORT_ITEM);
  public static final IonElementType IMPORT_PATH = new IonElementType("import_path", TypeId.IMPORT_PATH);
  public static final IonElementType EXPR_LITERAL_INT = new IonElementType("expr_literal_int", TypeId.EXPR_LITERAL_INT);
  public static final IonElementType EXPR_LITERAL_FLOAT = new IonElementType("expr_literal_float");
  public static final IonElementType EXPR_LITERAL_STR = new IonElementType("expr_literal_str");
  public static final IonElementType EXPR_LITERAL_CHAR = new IonElementType("expr_literal_char");
  public static final IonElementType EXPR_LITERAL_COMPOUND = new IonElementType("expr_literal_compound", TypeId.EXPR_LITERAL_COMPOUND);
  public static final IonElementType EXPR_LITERAL_COMPOUND_TYPED = new IonElementType("expr_literal_compound_typed", TypeId.EXPR_LITERAL_COMPOUND_TYPED);
  public static final IonElementType COMPOUND_FIELD = new IonElementType("compound_field", TypeId.COMPOUND_FIELD);
  public static final IonElementType COMPOUND_FIELD_NAMED = new IonElementType("compound_field_named", TypeId.COMPOUND_FIELD_NAMED);
  public static final IonElementType COMPOUND_FIELD_INDEX = new IonElementType("compound_field_index", TypeId.COMPOUND_FIELD_INDEX);
  public static final IonElementType EXPR_NAME = new IonElementType("expr_name", TypeId.EXPR_NAME);
  public static final IonElementType EXPR_NEW = new IonElementType("expr_new");
  public static final IonElementType EXPR_PAREN = new IonElementType("expr_paren");
  public static final IonElementType EXPR_CAST = new IonElementType("expr_cast");
  public static final IonElementType EXPR_POSTFIX = new IonElementType("expr_postfix");
  public static final IonElementType EXPR_INDEX = new IonElementType("expr_index", TypeId.EXPR_INDEX);
  public static final IonElementType EXPR_FIELD = new IonElementType("expr_field", TypeId.EXPR_FIELD);
  public static final IonElementType EXPR_CALL = new IonElementType("expr_call", TypeId.EXPR_CALL);
  public static final IonElementType EXPR_CALL_ARG = new IonElementType("expr_call_arg", TypeId.EXPR_CALL_ARG);
  public static final IonElementType EXPR_UNARY = new IonElementType("expr_unary", TypeId.EXPR_UNARY);
  public static final IonElementType EXPR_BINARY = new IonElementType("expr_binary");
  public static final IonElementType EXPR_TERNARY = new IonElementType("expr_ternary");
  public static final IonElementType TYPE_NAME = new IonElementType("type_name", TypeId.TYPE_NAME);
  public static final IonElementType TYPE_QNAME = new IonElementType("type_qname", TypeId.TYPE_QNAME);
  public static final IonElementType TYPE_PAR = new IonElementType("type_par", TypeId.TYPE_PAR);
  public static final IonElementType TYPE_PTR = new IonElementType("type_ptr", TypeId.TYPE_PTR);
  public static final IonElementType TYPE_CONST = new IonElementType("type_const", TypeId.TYPE_CONST);
  public static final IonElementType TYPE_ARRAY = new IonElementType("type_array", TypeId.TYPE_ARRAY);
  public static final IonElementType TYPE_TUPLE = new IonElementType("type_tuple", TypeId.TYPE_TUPLE);
  public static final IonElementType TYPE_FUNC = new IonElementType("type_func");
  public static final IonElementType TYPE_FUNC_PARAM = new IonElementType("type_func_param");
  public static final IonElementType STMT_LIST = new IonElementType("stmt_list");
  public static final IonElementType STMT_IF = new IonElementType("if");
  public static final IonElementType STMT_ELSE = new IonElementType("else");
  public static final IonElementType STMT_INIT = new IonElementType("stmt_init", TypeId.STMT_INIT);
  public static final IonElementType STMT_WHILE = new IonElementType("while");
  public static final IonElementType STMT_DO = new IonElementType("do");
  public static final IonElementType STMT_FOR = new IonElementType("for");
  public static final IonElementType STMT_SWITCH = new IonElementType("switch");
  public static final IonElementType STMT_SWITCH_PATTERN = new IonElementType("switch_pattern");
  public static final IonElementType STMT_BREAK = new IonElementType("break");
  public static final IonElementType STMT_CONTINUE = new IonElementType("continue");
  public static final IonElementType STMT_RETURN = new IonElementType("return", TypeId.STMT_RETURN);
  public static final IonElementType STMT_NOTE = new IonElementType("stmt_note");
  public static final IonElementType STMT_LABEL = new IonElementType("label", TypeId.STMT_LABEL);
  public static final IonElementType STMT_GOTO = new IonElementType("goto");
  public static final IonElementType LABEL_NAME = new IonElementType("label_name", TypeId.LABEL_NAME);
  public static final IonElementType STMT_EXPR = new IonElementType("stmt_expr");
  public static final IonElementType STMT_ASSIGN = new IonElementType("stmt_assign", TypeId.STMT_ASSIGN);
  public static final IonElementType STMT_SWITCH_CASE_BLOCK = new IonElementType("stmt_switch_case_block", TypeId.STMT_SWITCH_CASE_BLOCK);
  public static final IonBlockElementType STMT_BLOCK = new IonBlockElementType();

  // to be able to write switch over element types
  public enum TypeId {
    UNKNOWN,
    DECL_CONST,
    DECL_VAR,
    DECL_ENUM,
    DECL_ENUM_ITEM,
    DECL_AGGREGATE,
    DECL_FIELD,
    DECL_FIELD_NAME,
    DECL_IMPORT,
    DECL_IMPORT_ITEM,
    DECL_TYPEDEF,
    DECL_FUNC,
    DECL_FUNC_PARAM,
    DECL_NOTE,
    NOTE,
    NOTE_PARAM,
    IMPORT_PATH,
    EXPR_LITERAL_INT,
    EXPR_LITERAL_FLOAT,
    EXPR_LITERAL_STR,
    EXPR_LITERAL_CHAR,
    EXPR_LITERAL_COMPOUND,
    EXPR_LITERAL_COMPOUND_TYPED,
    COMPOUND_FIELD,
    COMPOUND_FIELD_NAMED,
    COMPOUND_FIELD_INDEX,
    EXPR_NAME,
    EXPR_NEW,
    EXPR_PAREN,
    EXPR_CAST,
    EXPR_POSTFIX,
    EXPR_INDEX,
    EXPR_FIELD,
    EXPR_CALL,
    EXPR_CALL_ARG,
    EXPR_UNARY,
    EXPR_BINARY,
    EXPR_TERNARY,
    TYPE_NAME,
    TYPE_QNAME,
    TYPE_PAR,
    TYPE_PTR,
    TYPE_CONST,
    TYPE_ARRAY,
    TYPE_TUPLE,
    TYPE_FUNC,
    TYPE_FUNC_PARAM,
    STMT_LIST,
    STMT_IF,
    STMT_ELSE,
    STMT_INIT,
    STMT_WHILE,
    STMT_DO,
    STMT_FOR,
    STMT_SWITCH,
    STMT_SWITCH_PATTERN,
    STMT_BREAK,
    STMT_CONTINUE,
    STMT_RETURN,
    STMT_NOTE,
    STMT_LABEL,
    STMT_GOTO,
    LABEL_NAME,
    STMT_EXPR,
    STMT_ASSIGN,
    STMT_SWITCH_CASE_BLOCK,
    STMT_BLOCK,
  }

  private final TypeId myTypeId;

  public IonElementType(@NotNull String debugName) {
    this(debugName, TypeId.UNKNOWN);
  }

  public IonElementType(@NotNull String debugName, TypeId id) {
    super(debugName, IonLanguage.INSTANCE);
    myTypeId = id;
  }

  public TypeId getTypeId() {
    return myTypeId;
  }

  @NotNull
  public PsiElement createPsiElement(ASTNode node) {
    return new IonPsiElement(node);
  }
}
