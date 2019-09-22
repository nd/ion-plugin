package ion;

import com.intellij.lexer.LexerBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ion.psi.IonToken.*;

public class IonLexer extends LexerBase {
  private final static IntIntTable KWM = new IntIntTable(256);
  private final static IElementType[] KW = new IElementType[IonToken.KEYWORDS.getTypes().length + 1];
  private final static int MAX_KW_LEN = ContainerUtil.map(IonToken.KEYWORDS.getTypes(), it -> it.toString().length())
          .stream().max(Integer::compareTo).get();
  private final static char[] KWCHARS = new char[MAX_KW_LEN * (IonToken.KEYWORDS.getTypes().length + 1)];

  static {
    byte i = 1;
    for (IElementType token : IonToken.KEYWORDS.getTypes()) {
      String t = token.toString();
      int hash = hash(t, 0, t.length());
      if (KWM.get(hash) != 0) {
        throw new IllegalStateException("Keyword hash collision");
      }
      KWM.put(hash, i);
      KW[i] = token;
      for (int j = i * MAX_KW_LEN, k = 0; k < t.length(); j++, k++) {
        KWCHARS[j] = t.charAt(k);
      }
      i++;
    }
  }

  private CharSequence myBuffer;
  private int myPosition;
  private int myEndOffset;
  private int myState;

  private int myTokenStart;
  private int myTokenEnd;
  private IElementType myToken;

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    myBuffer = buffer;
    myPosition = startOffset;
    myEndOffset = endOffset;
    myState = initialState;
    advance();
  }

  @Override
  public void advance() {
    if (myPosition >= myEndOffset) {
      myToken = null;
      return;
    }

    char c = myBuffer.charAt(myPosition);

    switch (c) {
      case ' ': case '\t': case '\n': case '\r':
        consumeWhitespace();
        break;

      case '\'':
        consumeChar();
        break;

      case '"':
        consumeString();
        break;

      case '.':
        if (Character.isDigit(charAt(1))) {
          consumeFloat();
        } else if (charAt(1) == '.' && charAt(2) == '.') {
          myTokenStart = myPosition;
          myPosition += 3;
          myTokenEnd = myPosition;
          myToken = ELLIPSIS;
        } else {
          myTokenStart = myPosition;
          myTokenEnd = ++myPosition;
          myToken = DOT;
        }
        break;

      case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
        int position = myPosition;
        IElementType token = myToken;
        int tokenStart = myTokenStart;
        int tokenEnd = myTokenEnd;
        consumeInt();
        if (charAt(0) == '.' && charAt(1) != '.' || charAt(0) == 'e') {
          myTokenStart = tokenStart;
          myTokenEnd = tokenEnd;
          myToken = token;
          myPosition = position;
          consumeFloat();
        }
        break;

      case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j':
      case 'k': case 'l': case 'm': case 'n': case 'o': case 'p': case 'q': case 'r': case 's': case 't':
      case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
      case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J':
      case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T':
      case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z':
      case '_':
        consumeName();
        break;

      case '/':
        scanDiv();
        break;
      case '(':
        consumeToken(LPAREN);
        break;
      case ')':
        consumeToken(RPAREN);
        break;
      case '{':
        consumeToken(LBRACE);
        break;
      case '}':
        consumeToken(RBRACE);
        break;
      case '[':
        consumeToken(LBRACKET);
        break;
      case ']':
        consumeToken(RBRACKET);
        break;
      case ',':
        consumeToken(COMMA);
        break;
      case '@':
        consumeToken(AT);
        break;
      case '#':
        consumeToken(POUND);
        break;
      case '?':
        consumeToken(QUESTION);
        break;
      case ';':
        consumeToken(SEMICOLON);
        break;
      case '~':
        consumeToken(NEG);
        break;
      case '!':
        consumeToken(NOT, '=', NOTEQ);
        break;
      case ':':
        consumeToken(COLON, '=', COLON_ASSIGN);
        break;
      case '=':
        consumeToken(ASSIGN, '=', EQ);
        break;
      case '^':
        consumeToken(XOR, '=', XOR_ASSIGN);
        break;
      case '*':
        consumeToken(MUL, '=', MUL_ASSIGN);
        break;
      case '%':
        consumeToken(MOD, '=', MOD_ASSIGN);
        break;
      case '+':
        consumeToken(ADD, '=', ADD_ASSIGN, '+', INC);
        break;
      case '-':
        consumeToken(SUB, '=', SUB_ASSIGN, '-', DEC);
        break;
      case '&':
        consumeToken(AND, '=', AND_ASSIGN, '&', AND_AND);
        break;
      case '|':
        consumeToken(OR, '=', OR_ASSIGN, '|', OR_OR);
        break;
      case '<':
        scanLt();
        break;
      case '>':
        scanGt();
        break;
      default:
        myTokenStart = myPosition;
        myTokenEnd = ++myPosition;
        myToken = TokenType.BAD_CHARACTER;
        break;
    }
  }

  private void scanLt() {
    char c = myBuffer.charAt(myPosition);
    assert c == '<';

    myTokenStart = myPosition;
    myPosition++;
    if (charAt(0) == '<') {
      myPosition++;
      if (charAt(0) == '=') {
        myPosition++;
        myToken = LSHIFT_ASSIGN;
      } else {
        myToken = LSHIFT;
      }
    } else if (charAt(0) == '=') {
      myToken = LTEQ;
      myPosition++;
    } else {
      myToken = LT;
    }
    myTokenEnd = myPosition;
  }

  private void scanGt() {
    myTokenStart = myPosition;
    myPosition++;
    if (charAt(0) == '>') {
      myPosition++;
      if (charAt(0) == '=') {
        myPosition++;
        myToken = RSHIFT_ASSIGN;
      } else {
        myToken = RSHIFT;
      }
    } else if (charAt(0) == '=') {
      myToken = GTEQ;
      myPosition++;
    } else {
      myToken = GT;
    }
    myTokenEnd = myPosition;
  }

  private void scanDiv() {
    char c = myBuffer.charAt(myPosition);
    assert c == '/';

    myTokenStart = myPosition;
    if (charAt(1) == '=') {
      myPosition += 2;
      myToken = DIV_ASSIGN;
      myTokenEnd = myPosition;
    } else if (charAt(1) == '/') {
      myPosition += 2;
      while (myPosition < myEndOffset) {
        c = myBuffer.charAt(myPosition);
        if (c == '\n') {
          break;
        } else {
          myPosition++;
        }
      }
      myToken = LINE_COMMENT;
      myTokenEnd = myPosition;
    } else if (charAt(1) == '*') {
      myPosition += 2;
      int level = 1;
      while (myPosition < myEndOffset && level > 0) {
        c = myBuffer.charAt(myPosition);
        if (c == '/' && charAt(1) == '*') {
          level++;
          myPosition += 2;
        } else if (c == '*' && charAt(1) == '/') {
          level--;
          myPosition += 2;
        } else {
          myPosition++;
        }
      }
      myToken = BLOCK_COMMENT;
      myTokenEnd = myPosition;
    } else {
      myToken = DIV;
      myTokenEnd = ++myPosition;
    }
  }

  private void consumeToken(@NotNull IElementType token) {
    myTokenStart = myPosition;
    myTokenEnd = ++myPosition;
    myToken = token;
  }

  private void consumeToken(IElementType token1, char c, IElementType token2) {
    myTokenStart = myPosition;
    myPosition++;
    IElementType token;
    if (charAt(0) == c) {
      myPosition++;
      token = token2;
    } else {
      token = token1;
    }
    myTokenEnd = myPosition;
    myToken = token;
  }

  private void consumeToken(IElementType token1, char c2, IElementType token2, char c3, IElementType token3) {
    myTokenStart = myPosition;
    myPosition++;
    IElementType token;
    if (charAt(0) == c2) {
      myPosition++;
      token = token2;
    } else if (charAt(0) == c3) {
      myPosition++;
      token = token3;
    } else {
      token = token1;
    }
    myTokenEnd = myPosition;
    myToken = token;
  }

  private void consumeName() {
    myTokenStart = myPosition;
    while (myPosition < myEndOffset) {
      char c = myBuffer.charAt(myPosition);
      if (!Character.isLetterOrDigit(c) && c != '_')
        break;
      myPosition++;
    }

    myTokenEnd = myPosition;
    myToken = keywordOrName(myBuffer, myTokenStart, myTokenEnd);
  }

  private static IElementType keywordOrName(CharSequence buf, int start, int end) {
    if (end - start > MAX_KW_LEN) return NAME;
    int hash = hash(buf, start, end);
    int index = KWM.get(hash);
    return index != 0 && equals(buf, start, end, index) ? KW[index] : NAME;
  }

  private static boolean equals(CharSequence buf, int start, int end, int index) {
    for (int i = start, j = index * MAX_KW_LEN; i < end; i++, j++) {
      if (buf.charAt(i) != KWCHARS[j]) {
        return false;
      }
    }
    return true;
  }

  private static int hash(CharSequence buf, int start, int end) {
    return StringUtil.stringHashCode(buf, start, end);
  }

  private void consumeFloat() {
    char c = myBuffer.charAt(myPosition);
    assert Character.isDigit(c) || c == '.';

    myTokenStart = myPosition;
    while (myPosition < myEndOffset) {
      c = myBuffer.charAt(myPosition);
      if (!Character.isDigit(c))
        break;
      myPosition++;
    }

    if (c == '.') {
      myPosition++;
    }

    while (myPosition < myEndOffset) {
      c = myBuffer.charAt(myPosition);
      if (!Character.isDigit(c))
        break;
      myPosition++;
    }

    if (c == 'e' || c == 'E') {
      myPosition++;
      if (charAt(0) == '+' || charAt(0) == '-') {
        myPosition++;
      }
      while (myPosition < myEndOffset) {
        c = myBuffer.charAt(myPosition);
        if (!Character.isDigit(c))
          break;
        myPosition++;
      }
    }
    if (charAt(0) == 'd') {
      myPosition++;
    }
    myTokenEnd = myPosition;
    myToken = FLOAT;
  }

  private void consumeInt() {
    char c = myBuffer.charAt(myPosition);
    assert Character.isDigit(c);

    myTokenStart = myPosition;
    if (c == '0') {
      int c1 = charAt(1);
      if (c1 == 'x' || c1 == 'X') {
        myPosition += 2;
        consumeHex();
      }
      else if (c1 == 'b' || c1 == 'B') {
        myPosition += 2;
        consumeBin();
      } else {
        myPosition++;
        consumeOct();
      }
    } else {
      consumeDec();
    }
    loop:
    while (true) {
      switch (charAt(0)) {
        case 'u': case 'U': case 'l': case 'L':
          myPosition++;
          break;
        default:
          break loop;
      }
    }

    myTokenEnd = myPosition;
    myToken = INT;
  }

  private void consumeHex() {
    while (myPosition < myEndOffset) {
      char c = myBuffer.charAt(myPosition);
      if (c != '_' && !isHex(c))
        break;
      myPosition++;
    }
  }

  private void consumeDec() {
    while (myPosition < myEndOffset) {
      char c = myBuffer.charAt(myPosition);
      if (c != '_' && !Character.isDigit(c))
        break;
      myPosition++;
    }
  }

  private void consumeOct() {
    while (myPosition < myEndOffset) {
      char c = myBuffer.charAt(myPosition);
      if (c != '_' && !isOctal(c))
        break;
      myPosition++;
    }
  }

  private void consumeBin() {
    while (myPosition < myEndOffset) {
      char c = myBuffer.charAt(myPosition);
      if (c != '_' && c != '0' && c != '1')
        break;
      myPosition++;
    }
  }

  private static boolean isHex(char c) {
    return Character.isDigit(c) || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E'|| c == 'F'
            || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e'|| c == 'f';
  }

  private static boolean isOctal(char c) {
    switch (c) {
      case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':
        return true;
      default:
        return false;
    }
  }

  private void consumeString() {
    char c = myBuffer.charAt(myPosition);
    assert c == '"';

    if (charAt(1) == '"' && charAt(2) == '"') {
      myTokenStart = myPosition;
      myPosition += 3;
      while (myPosition < myEndOffset) {
        if (charAt(0) == '"' && charAt(1) == '"' && charAt(2) == '"') {
          myPosition += 3;
          break;
        } else {
          myPosition++;
        }
      }
      myTokenEnd = myPosition;
      myToken = MULTILINE_STRING;
    } else {
      myTokenStart = myPosition;
      myPosition++;
      while (myPosition < myEndOffset) {
        c = myBuffer.charAt(myPosition);
        if (c == '"' || c == '\n') {
          break;
        } else if (c == '\\') {
          myPosition++;
        }
        myPosition++;
      }

      if (c == '"') {
        myPosition++;
      }
      myToken = STRING;
      myTokenEnd = myPosition;
    }
  }

  private int charAt(int offset) {
    int position = myPosition + offset;
    return position < myEndOffset ? myBuffer.charAt(position) : -1;
  }

  private void consumeChar() {
    int c = myBuffer.charAt(myPosition);
    assert c == '\'';

    myTokenStart = myPosition;
    myPosition++;
    while (myPosition < myEndOffset) {
      c = myBuffer.charAt(myPosition);
      if (c == '\'' || c == '\n') {
        break;
      } else if (c == '\\') {
        myPosition++;
      }
      myPosition++;
    }

    if (charAt(0) == '\'') {
      myPosition++;
    }
    myTokenEnd = myPosition;
    myToken = CHAR;
  }

  private void consumeWhitespace() {
    char c = myBuffer.charAt(myPosition);
    assert isWhitespace(c);
    myTokenStart = myPosition;
    while (myPosition < myEndOffset) {
      c = myBuffer.charAt(myPosition);
      if (!isWhitespace(c))
        break;
      myPosition++;
    }
    myTokenEnd = myPosition;
    myToken = TokenType.WHITE_SPACE;
  }

  private static boolean isWhitespace(char c) {
    return c == ' ' || c == '\t' || c == '\n' || c == '\r';
  }

  @Override
  public int getState() {
    return myState;
  }

  @Nullable
  @Override
  public IElementType getTokenType() {
    return myToken;
  }

  @Override
  public int getTokenStart() {
    return myTokenStart;
  }

  @Override
  public int getTokenEnd() {
    return myTokenEnd;
  }

  @NotNull
  @Override
  public CharSequence getBufferSequence() {
    return myBuffer;
  }

  @Override
  public int getBufferEnd() {
    return myEndOffset;
  }
}