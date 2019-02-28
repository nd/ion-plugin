package ion.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonToken extends IElementType {
  public static IonToken NL = new IonToken("NEWLINE");
  public static IonToken LINE_COMMENT = new IonToken("line_comment");
  public static IonToken BLOCK_COMMENT = new IonToken("block_comment");
  public static IonToken COLON = new IonToken(":");
  public static IonToken LPAREN = new IonToken("(");
  public static IonToken RPAREN = new IonToken(")");
  public static IonToken LBRACE = new IonToken("{");
  public static IonToken RBRACE = new IonToken("}");
  public static IonToken LBRACKET = new IonToken("[");
  public static IonToken RBRACKET = new IonToken("]");
  public static IonToken COMMA = new IonToken(",");
  public static IonToken DOT = new IonToken(".");
  public static IonToken AT = new IonToken("@");
  public static IonToken POUND = new IonToken("#");
  public static IonToken ELLIPSIS = new IonToken("...");
  public static IonToken QUESTION = new IonToken("?");
  public static IonToken SEMICOLON = new IonToken(";");
  public static IonToken NEG = new IonToken("~");
  public static IonToken NOT = new IonToken("!");
  public static IonToken MUL = new IonToken("*");
  public static IonToken DIV = new IonToken("/");
  public static IonToken MOD = new IonToken("%");
  public static IonToken AND = new IonToken("&");
  public static IonToken LSHIFT = new IonToken("<<");
  public static IonToken RSHIFT = new IonToken(">>");
  public static IonToken ADD = new IonToken("+");
  public static IonToken SUB = new IonToken("-");
  public static IonToken OR = new IonToken("|");
  public static IonToken XOR = new IonToken("^");
  public static IonToken EQ = new IonToken("==");
  public static IonToken NOTEQ = new IonToken("!=");
  public static IonToken LT = new IonToken("<");
  public static IonToken GT = new IonToken(">");
  public static IonToken LTEQ = new IonToken("<=");
  public static IonToken GTEQ = new IonToken(">=");
  public static IonToken AND_AND = new IonToken("&&");
  public static IonToken OR_OR = new IonToken("||");
  public static IonToken ASSIGN = new IonToken("=");
  public static IonToken ADD_ASSIGN = new IonToken("+=");
  public static IonToken SUB_ASSIGN = new IonToken("-=");
  public static IonToken OR_ASSIGN = new IonToken("|=");
  public static IonToken AND_ASSIGN = new IonToken("&=");
  public static IonToken XOR_ASSIGN = new IonToken("^=");
  public static IonToken MUL_ASSIGN = new IonToken("*=");
  public static IonToken DIV_ASSIGN = new IonToken("/=");
  public static IonToken MOD_ASSIGN = new IonToken("%=");
  public static IonToken LSHIFT_ASSIGN = new IonToken("<<=");
  public static IonToken RSHIFT_ASSIGN = new IonToken(">>=");
  public static IonToken INC = new IonToken("++");
  public static IonToken DEC = new IonToken("--");
  public static IonToken COLON_ASSIGN = new IonToken(":=");

  public static IonToken TYPEDEF = new IonToken("typedef");
  public static IonToken ENUM = new IonToken("enum");
  public static IonToken STRUCT = new IonToken("struct");
  public static IonToken UNION = new IonToken("union");
  public static IonToken VAR = new IonToken("var");
  public static IonToken CONST = new IonToken("const");
  public static IonToken FUNC = new IonToken("func");
  public static IonToken SIZEOF = new IonToken("sizeof");
  public static IonToken TYPEOF = new IonToken("typeof");
  public static IonToken ALIGNOF = new IonToken("alignof");
  public static IonToken OFFSETOF = new IonToken("offsetof");
  public static IonToken BREAK = new IonToken("break");
  public static IonToken CONTINUE = new IonToken("continue");
  public static IonToken RETURN = new IonToken("return");
  public static IonToken IF = new IonToken("if");
  public static IonToken ELSE = new IonToken("else");
  public static IonToken WHILE = new IonToken("while");
  public static IonToken DO = new IonToken("do");
  public static IonToken FOR = new IonToken("for");
  public static IonToken SWITCH = new IonToken("switch");
  public static IonToken CASE = new IonToken("case");
  public static IonToken DEFAULT = new IonToken("default");
  public static IonToken GOTO = new IonToken("goto");
  public static IonToken IMPORT = new IonToken("import");
  public static IonToken NEW = new IonToken("new");
  public static IonToken UNDEF = new IonToken("undef");

  public static IonToken CHAR = new IonToken("char");
  public static IonToken STRING = new IonToken("string");
  public static IonToken MULTILINE_STRING = new IonToken("multiline_string");
  public static IonToken INT = new IonToken("int");
  public static IonToken FLOAT = new IonToken("float");
  public static IonToken NAME = new IonToken("name");

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

  private IonToken(@NotNull String debugName) {
    super(debugName, IonLanguage.INSTANCE);
  }

}
