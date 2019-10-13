package ion;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static ion.psi.IonElementType.*;
import static ion.psi.IonToken.*;

public class IonParser implements PsiParser {
  @Override
  public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    parseFile(b);
    m.done(root);
    return b.getTreeBuilt();
  }

  private void parseFile(@NotNull PsiBuilder b) {
    IElementType token;
    while ((token = b.getTokenType()) != null) {
      if (token == IMPORT) {
        parseImport(b);
      } else if (token == VAR) {
        parseVar(b);
      } else {
        b.advanceLexer();
      }
    }
  }

  private void parseImport(@NotNull PsiBuilder b) {
    assert b.getTokenType() == IMPORT;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    consume(b, DOT);
    if (expect(b, NAME)) {
      if (consume(b, ASSIGN)) {
        consume(b, DOT);
        expect(b, NAME);
      }
      while (consume(b, DOT)) {
        expect(b, NAME);
      }
      if (consume(b, LBRACE)) {
        while (match(b, ELLIPSIS, NAME)) {
          parseImportItem(b);
          if (!consume(b, COMMA)) {
            break;
          }
        }
        expect(b, RBRACE);
      }
    }
    m.done(IMPORT_DECL);
    return;
  }

  private void parseImportItem(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (consume(b, NAME)) {
      if (consume(b, ASSIGN)) {
        expect(b, NAME);
      }
    } else {
      expect(b, ELLIPSIS);
    }
    m.done(IMPORT_ITEM);
  }

  private void parseVar(@NotNull PsiBuilder b) {
    assert b.getTokenType() == VAR;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (expect(b, NAME)) {
      //todo type
      if (consume(b, ASSIGN)) {
        if (!parseExpr(b)) {
          b.error("Exprected expression, got " + b.getTokenText());
          b.advanceLexer();
        }
      }
      expect(b, SEMICOLON);
    }
    m.done(VAR_DECL);
  }

  private boolean parseExpr(@NotNull PsiBuilder b) {
    return parseExprTernary(b);
  }

  private boolean parseExprTernary(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprOr(b)) {
      m.drop();
      return false;
    }
    if (consume(b, QUESTION)) {
      parseExprTernary(b);
      expect(b, COLON);
      parseExprTernary(b);
      m.done(EXPR_TERNARY);
      return true;
    } else {
      m.drop();
      return true;
    }
  }

  private boolean parseExprOr(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprAnd(b)) {
      m.drop();
      return false;
    }
    while (OR_OR == b.getTokenType()) {
      PsiBuilder.Marker outer = m.precede();
      b.advanceLexer();
      parseExprAnd(b);
      m.done(EXPR_BINARY);
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprAnd(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprCmp(b)) {
      m.drop();
      return false;
    }
    while (AND_AND == b.getTokenType()) {
      PsiBuilder.Marker outer = m.precede();
      b.advanceLexer();
      parseExprCmp(b);
      m.done(EXPR_BINARY);
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprCmp(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprAdd(b)) {
      m.drop();
      return false;
    }
    while (CMP_OP.contains(b.getTokenType())) {
      PsiBuilder.Marker outer = m.precede();
      b.advanceLexer();
      parseExprAdd(b);
      m.done(EXPR_BINARY);
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprAdd(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprMul(b)) {
      m.drop();
      return false;
    }
    while (ADD_OP.contains(b.getTokenType())) {
      PsiBuilder.Marker outer = m.precede();
      b.advanceLexer();
      parseExprMul(b);
      m.done(EXPR_BINARY);
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprMul(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprUnary(b)) {
      m.drop();
      return false;
    }
    while (MUL_OP.contains(b.getTokenType())) {
      PsiBuilder.Marker outer = m.precede();
      b.advanceLexer();
      parseExprUnary(b);
      m.done(EXPR_BINARY);
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprUnary(@NotNull PsiBuilder b) {
    if (UNARY_OP.contains(b.getTokenType())) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      parseExprUnary(b);
      m.done(EXPR_UNARY);
      return true;
    } else {
      return parseExprBase(b);
    }
  }

  private boolean parseExprBase(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseExprOperand(b)) {
      m.drop();
      return false;
    }
    while (match(b, LPAREN, LBRACKET, DOT, INC, DEC)) {
      PsiBuilder.Marker outer = m.precede();
      if (consume(b, LPAREN)) {
        if (!match(b, RPAREN)) {
          parseExpr(b);
          while (consume(b, COMMA)) {
            parseExpr(b);
          }
        }
        expect(b, RPAREN);
        m.done(EXPR_CALL);
      } else if (consume(b, LBRACKET)) {
        if (!parseExpr(b)) {
          b.error("Expected expression, got " + b.getTokenText());
          b.advanceLexer();
        }
        expect(b, RBRACKET);
        m.done(EXPR_INDEX);
      } else if (consume(b, DOT)) {
        expect(b, NAME);
        m.done(EXPR_FIELD);
      } else if (consume(b, INC) || consume(b, DEC)) {
        m.done(EXPR_POSTFIX);
      }
      m = outer;
    }
    m.drop();
    return true;
  }

  private boolean parseExprOperand(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (consume(b, INT)) {
      m.done(EXPR_LITERAL_INT);
      return true;
    }
    if (consume(b, FLOAT)) {
      m.done(EXPR_LITERAL_FLOAT);
      return true;
    }
    if (consume(b, STRING)) {
      m.done(EXPR_LITERAL_STR);
      return true;
    }
    if (consume(b, NAME)) {
      if (match(b, LBRACE)) {
        //todo parse compound literal
      } else {
        m.done(EXPR_NAME);
        return true;
      }
    }
    if (consume(b, NEW)) {
      if (consume(b, LPAREN)) {
        parseExpr(b);
        expect(b, RPAREN);
      }
      if (consume(b, LBRACKET)) {
        parseExpr(b);
        expect(b, RBRACKET);
      }
      if (!consume(b, UNDEF)) {
        parseExpr(b);
      }
      m.done(EXPR_NEW);
      return true;
    }
    // todo next sizeof
    // todo alignof
    // todo typeof
    // todo offsetof
    // todo lbrace - compound without type
    if (consume(b, LPAREN)) {
      // todo colon - case
      parseExpr(b);
      expect(b, RPAREN);
      m.done(EXPR_PAREN);
      return true;
    }
    m.drop();
    return false;
  }

  private boolean expect(@NotNull PsiBuilder b, @NotNull IElementType token) {
    boolean matched = true;
    if (b.getTokenType() != token) {
      b.error("Expected " + token.toString() + ", got " + b.getTokenText());
      matched = false;
    }
    b.advanceLexer();
    return matched;
  }

  private boolean consume(@NotNull PsiBuilder b, @NotNull IElementType token) {
    if (b.getTokenType() == token) {
      b.advanceLexer();
      return true;
    } else {
      return false;
    }
  }

  private boolean match(@NotNull PsiBuilder b, @NotNull IElementType... tokens) {
    IElementType actual = b.getTokenType();
    for (IElementType token : tokens) {
      if (token == actual) {
        return true;
      }
    }
    return false;
  }
}
