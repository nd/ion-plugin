package ion.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonToken extends IElementType {
  public static final IonToken LINE_COMMENT = new IonToken("line_comment");
  public static final IonToken BLOCK_COMMENT = new IonToken("block_comment");
  public static final IonToken COLON = new IonToken(":");
  public static final IonToken LPAREN = new IonToken("(");
  public static final IonToken RPAREN = new IonToken(")");
  public static final IonToken LBRACE = new IonToken("{");
  public static final IonToken RBRACE = new IonToken("}");
  public static final IonToken LBRACKET = new IonToken("[");
  public static final IonToken RBRACKET = new IonToken("]");
  public static final IonToken COMMA = new IonToken(",");
  public static final IonToken DOT = new IonToken(".");
  public static final IonToken AT = new IonToken("@");
  public static final IonToken POUND = new IonToken("#");
  public static final IonToken ELLIPSIS = new IonToken("...");
  public static final IonToken QUESTION = new IonToken("?");
  public static final IonToken SEMICOLON = new IonToken(";");
  public static final IonToken NEG = new IonToken("~");
  public static final IonToken NOT = new IonToken("!");
  public static final IonToken MUL = new IonToken("*");
  public static final IonToken DIV = new IonToken("/");
  public static final IonToken MOD = new IonToken("%");
  public static final IonToken AND = new IonToken("&");
  public static final IonToken LSHIFT = new IonToken("<<");
  public static final IonToken RSHIFT = new IonToken(">>");
  public static final IonToken ADD = new IonToken("+");
  public static final IonToken SUB = new IonToken("-");
  public static final IonToken OR = new IonToken("|");
  public static final IonToken XOR = new IonToken("^");
  public static final IonToken EQ = new IonToken("==");
  public static final IonToken NOTEQ = new IonToken("!=");
  public static final IonToken LT = new IonToken("<");
  public static final IonToken GT = new IonToken(">");
  public static final IonToken LTEQ = new IonToken("<=");
  public static final IonToken GTEQ = new IonToken(">=");
  public static final IonToken AND_AND = new IonToken("&&");
  public static final IonToken OR_OR = new IonToken("||");
  public static final IonToken ASSIGN = new IonToken("=");
  public static final IonToken ADD_ASSIGN = new IonToken("+=");
  public static final IonToken SUB_ASSIGN = new IonToken("-=");
  public static final IonToken OR_ASSIGN = new IonToken("|=");
  public static final IonToken AND_ASSIGN = new IonToken("&=");
  public static final IonToken XOR_ASSIGN = new IonToken("^=");
  public static final IonToken MUL_ASSIGN = new IonToken("*=");
  public static final IonToken DIV_ASSIGN = new IonToken("/=");
  public static final IonToken MOD_ASSIGN = new IonToken("%=");
  public static final IonToken LSHIFT_ASSIGN = new IonToken("<<=");
  public static final IonToken RSHIFT_ASSIGN = new IonToken(">>=");
  public static final IonToken INC = new IonToken("++");
  public static final IonToken DEC = new IonToken("--");
  public static final IonToken COLON_ASSIGN = new IonToken(":=");

  public static final IonToken TYPEDEF = new IonToken("typedef");
  public static final IonToken ENUM = new IonToken("enum");
  public static final IonToken STRUCT = new IonToken("struct");
  public static final IonToken UNION = new IonToken("union");
  public static final IonToken VAR = new IonToken("var");
  public static final IonToken CONST = new IonToken("const");
  public static final IonToken FUNC = new IonToken("func");
  public static final IonToken SIZEOF = new IonToken("sizeof");
  public static final IonToken TYPEOF = new IonToken("typeof");
  public static final IonToken ALIGNOF = new IonToken("alignof");
  public static final IonToken OFFSETOF = new IonToken("offsetof");
  public static final IonToken BREAK = new IonToken("break");
  public static final IonToken CONTINUE = new IonToken("continue");
  public static final IonToken RETURN = new IonToken("return");
  public static final IonToken IF = new IonToken("if");
  public static final IonToken ELSE = new IonToken("else");
  public static final IonToken WHILE = new IonToken("while");
  public static final IonToken DO = new IonToken("do");
  public static final IonToken FOR = new IonToken("for");
  public static final IonToken SWITCH = new IonToken("switch");
  public static final IonToken CASE = new IonToken("case");
  public static final IonToken DEFAULT = new IonToken("default");
  public static final IonToken GOTO = new IonToken("goto");
  public static final IonToken IMPORT = new IonToken("import");
  public static final IonToken NEW = new IonToken("new");
  public static final IonToken UNDEF = new IonToken("undef");

  public static final IonToken CHAR = new IonToken("char");
  public static final IonToken STRING = new IonToken("string");
  public static final IonToken MULTILINE_STRING = new IonToken("multiline_string");
  public static final IonToken INT = new IonToken("int");
  public static final IonToken FLOAT = new IonToken("float");
  public static final IonToken NAME = new IonToken("name");

  public final static TokenSet KEYWORDS = TokenSet.create(
          IonToken.TYPEDEF,
          IonToken.ENUM,
          IonToken.STRUCT,
          IonToken.UNION,
          IonToken.VAR,
          IonToken.CONST,
          IonToken.FUNC,
          IonToken.SIZEOF,
          IonToken.TYPEOF,
          IonToken.ALIGNOF,
          IonToken.OFFSETOF,
          IonToken.BREAK,
          IonToken.CONTINUE,
          IonToken.RETURN,
          IonToken.IF,
          IonToken.ELSE,
          IonToken.WHILE,
          IonToken.DO,
          IonToken.FOR,
          IonToken.SWITCH,
          IonToken.CASE,
          IonToken.DEFAULT,
          IonToken.GOTO,
          IonToken.IMPORT,
          IonToken.NEW,
          IonToken.UNDEF
  );

  public final static TokenSet OPERATORS = TokenSet.create(
          IonToken.ADD, IonToken.SUB, IonToken.XOR, IonToken.OR,
          IonToken.MUL, IonToken.DIV, IonToken.MOD, IonToken.AND, IonToken.LSHIFT, IonToken.RSHIFT,
          IonToken.OR_OR,
          IonToken.AND_AND,
          IonToken.EQ, IonToken.NOTEQ, IonToken.LT, IonToken.GT, IonToken.LTEQ, IonToken.GTEQ,
          IonToken.ADD, IonToken.SUB, IonToken.MUL, IonToken.AND,
          IonToken.NEG, IonToken.NOT, IonToken.INC, IonToken.DEC
  );

  public final static TokenSet UNARY_OP = TokenSet.create(
          IonToken.ADD, IonToken.SUB, IonToken.MUL, IonToken.AND,
          IonToken.NEG, IonToken.NOT, IonToken.INC, IonToken.DEC
  );

  public final static TokenSet MUL_OP = TokenSet.create(
          IonToken.MUL, IonToken.DIV, IonToken.MOD, IonToken.AND,
          IonToken.LSHIFT, IonToken.RSHIFT
  );

  public final static TokenSet ADD_OP = TokenSet.create(
          IonToken.ADD, IonToken.SUB, IonToken.OR, IonToken.XOR
  );

  public final static TokenSet CMP_OP = TokenSet.create(
          IonToken.LT, IonToken.LTEQ, IonToken.GT, IonToken.GTEQ,
          IonToken.EQ, IonToken.NOTEQ
  );

  public final static TokenSet COMMENTS = TokenSet.create(BLOCK_COMMENT, LINE_COMMENT);

  public final static TokenSet STRINGS = TokenSet.create(STRING, MULTILINE_STRING);

  public final static TokenSet ASSIGN_OP = TokenSet.create(
          ASSIGN, ADD_ASSIGN, SUB_ASSIGN, OR_ASSIGN, AND_ASSIGN, XOR_ASSIGN,
          MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, LSHIFT_ASSIGN, RSHIFT_ASSIGN
  );

  private IonToken(@NotNull String debugName) {
    super(debugName, IonLanguage.INSTANCE);
  }
}
