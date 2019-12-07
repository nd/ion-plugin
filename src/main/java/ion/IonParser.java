package ion;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import ion.psi.IonElementType;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static ion.psi.IonElementType.*;
import static ion.psi.IonToken.*;

public class IonParser implements PsiParser {
  private final static TokenSet TOP_LEVEL_TOKENS = TokenSet.create(
          ENUM, FUNC, TYPEDEF, VAR, CONST, UNION, IMPORT, SEMICOLON, AT, POUND
  );

  private final static TokenSet FUNC_PARAMS_STOP = TokenSet.create(
          LBRACE, SEMICOLON
  );


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
      if (token == ENUM) {
        parseEnum(b);
      } else if (token == STRUCT || token == UNION) {
        parseAggregateDecl(b);
      } else if (token == IMPORT) {
        parseImport(b);
      } else if (token == CONST) {
        parseVarOrConst(b, CONST, DECL_CONST);
      } else if (token == VAR) {
        parseVarOrConst(b, VAR, DECL_VAR);
      } else if (token == TYPEDEF) {
        parseTypedef(b);
      } else if (token == FUNC) {
        parseFunc(b);
      } else if (token == AT || token == POUND) {
        parseNote(b);
      } else {
        b.error("Exprected declaration or note, got " + b.getTokenText());
        b.advanceLexer();
      }
    }
  }

  private void parseEnum(@NotNull PsiBuilder b) {
    assert b.getTokenType() == ENUM;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    consume(b, NAME);
    if (consume(b, ASSIGN)) {
      if (!expectType(b)) {
        b.advanceLexer();
      }
    }
    if (expect(b, LBRACE)) {
      while (!b.eof() && !match(b, RBRACE)) {
        if (!parseEnumItem(b)) {
          b.error("Expected name, got " + b.getTokenText());
          break;
        }
        if (!consume(b, COMMA)) {
          if (!match(b, RBRACE)) {
            b.error("Exprected ',', got " + b.getTokenText());
          }
          break;
        }
      }
      consumeUntil(b, RBRACE, TOP_LEVEL_TOKENS);
    }
    m.done(DECL_ENUM);
  }

  private boolean parseEnumItem(@NotNull PsiBuilder b) {
    if (match(b, NAME)) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      if (consume(b, ASSIGN)) {
        expectExpr(b);
      }
      m.done(DECL_ENUM_ITEM);
      return true;
    } else {
      return false;
    }
  }

  private void parseAggregateDecl(@NotNull PsiBuilder b) {
    assert b.getTokenType() == STRUCT || b.getTokenType() == UNION;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    consume(b, NAME);
    if (!consume(b, SEMICOLON)) {
      parseAggregate(b);
    }
    m.done(DECL_AGGREGATE);
  }

  private void parseAggregate(@NotNull PsiBuilder b) {
    if (expect(b, LBRACE)) {
      while (!b.eof() && !match(b, RBRACE)) {
        parseAggregateItem(b);
        if (!match(b, RBRACE) && !match(b, NAME) && !match(b, STRUCT) && !match(b, UNION)) {
          b.error("Exprected '}', or aggregate item, got " + b.getTokenText());
          break;
        }
      }
      consumeUntil(b, RBRACE, TOP_LEVEL_TOKENS);
    }
  }

  private void parseAggregateItem(@NotNull PsiBuilder b) {
    if (match(b, STRUCT) || match(b, UNION)) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      parseAggregate(b);
      m.done(DECL_FIELD);
    }
    if (match(b, NAME)) {
      PsiBuilder.Marker m = b.mark();
      PsiBuilder.Marker name = b.mark();
      consume(b, NAME);
      name.done(DECL_FIELD_NAME);
      while (consume(b, COMMA)) {
        if (match(b, NAME)) {
          name = b.mark();
          consume(b, NAME);
          name.done(DECL_FIELD_NAME);
        } else {
          b.error("Expected name, got " + b.getTokenText());
          break;
        }
      }
      expect(b, COLON);
      expectType(b);
      expect(b, SEMICOLON);
      m.done(DECL_FIELD);
    }
  }

  private void parseImport(@NotNull PsiBuilder b) {
    assert b.getTokenType() == IMPORT;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    consume(b, DOT);
    if (match(b, NAME)) {
      if (lookAhead(b, 1, ASSIGN)) {
        consume(b, NAME);
        consume(b, ASSIGN);
      }
      PsiBuilder.Marker path = b.mark();
      consume(b, DOT);
      while (expect(b, NAME)) {
        if (match(b, DOT)) {
          PsiBuilder.Marker outer = path.precede();
          path.done(IMPORT_PATH);
          path = outer;
          consume(b, DOT);
        } else {
          break;
        }
      }
      path.done(IMPORT_PATH);

      if (consume(b, LBRACE)) {
        while (match(b, ELLIPSIS, NAME)) {
          parseImportItem(b);
          if (!consume(b, COMMA)) {
            break;
          }
        }
        consumeUntil(b, RBRACE);
      }
    }
    m.done(DECL_IMPORT);
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
    m.done(DECL_IMPORT_ITEM);
  }

  private void parseVarOrConst(@NotNull PsiBuilder b,
                               @NotNull IonToken varOrConst,
                               @NotNull IonElementType elementType) {
    assert (varOrConst == CONST || varOrConst == VAR) && b.getTokenType() == varOrConst;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (expect(b, NAME)) {
      if (consume(b, COLON)) {
        expectType(b);
      }
      if (consume(b, ASSIGN)) {
        expectExpr(b);
      }
    }
    consumeUntil(b, SEMICOLON, TOP_LEVEL_TOKENS);
    m.done(elementType);
  }

  private void parseTypedef(@NotNull PsiBuilder b) {
    assert b.getTokenType() == TYPEDEF;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, NAME);
    expect(b, ASSIGN);
    expectType(b);
    consumeUntil(b, SEMICOLON, TOP_LEVEL_TOKENS);
    m.done(DECL_TYPEDEF);
  }

  private void parseFunc(@NotNull PsiBuilder b) {
    assert b.getTokenType() == FUNC;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, NAME);
    if (expect(b, LPAREN)) {
      if (!b.eof() && !match(b, RPAREN)) {
        do {
          if (!parseFuncParam(b)) {
            b.error("Expected function paramter, got " + b.getTokenText());
            break;
          }
          if (!consume(b, COMMA)) {
            if (!match(b, RPAREN)) {
              b.error("Expected ',', or ')', got " + b.getTokenText());
            }
            break;
          }
        } while (true);
      }
      consumeUntil(b, RPAREN, FUNC_PARAMS_STOP);
    }
    if (consume(b, COLON)) {
      expectType(b);
    }
    if (match(b, LBRACE)) {
      expectStmtBlock(b);
    } else if (!consume(b, SEMICOLON)) {
      b.error("Expected ';' or block, got " + b.getTokenText());
    }
    m.done(DECL_FUNC);
  }

  private boolean parseFuncParam(@NotNull PsiBuilder b) {
    boolean name = match(b, NAME);
    if (name || match(b, ELLIPSIS)) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      if (name) {
        expect(b, COLON);
        expectType(b);
      } else {
        if (!match(b, RPAREN) && !match(b, COMMA)) {
          expectType(b);
        }
      }
      m.done(DECL_FUNC_PARAM);
      return true;
    } else {
      return false;
    }
  }

  public void expectStmtBlock(@NotNull PsiBuilder b) {
    if (match(b, LBRACE)) {
      PsiBuilder.Marker m = b.mark();
      consume(b, LBRACE);
      parseStmtList(b);
      expect(b, RBRACE);
      m.done(STMT_BLOCK);
    } else {
      b.error("Expected block, got " + b.getTokenText());
    }
  }

  private void parseStmtList(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    while (!b.eof() && !match(b, RBRACE)) {
      if (!parseStmt(b) && !match(b, RBRACE)) {
        b.error("Expected statement, got " + b.getTokenText());
        b.advanceLexer();
      }
    }
    m.done(STMT_LIST);
  }

  private boolean parseStmt(@NotNull PsiBuilder b) {
    if (b.getTokenType() == IF) {
      parseStmtIf(b);
      return true;
    }
    if (b.getTokenType() == WHILE) {
      parseStmtWhile(b);
      return true;
    }
    if (b.getTokenType() == DO) {
      parseStmtDo(b);
      return true;
    }
    if (b.getTokenType() == FOR) {
      parseStmtFor(b);
      return true;
    }
    if (b.getTokenType() == SWITCH) {
      parseStmtSwitch(b);
      return true;
    }
    if (b.getTokenType() == LBRACE) {
      expectStmtBlock(b);
      return true;
    }
    if (b.getTokenType() == BREAK) {
      parseStmtBreak(b);
      return true;
    }
    if (b.getTokenType() == CONTINUE) {
      parseStmtContinue(b);
      return true;
    }
    if (b.getTokenType() == RETURN) {
      parseStmtReturn(b);
      return true;
    }
    if (b.getTokenType() == POUND || b.getTokenType() == AT) {
      parseStmtNote(b);
      return true;
    }
    if (b.getTokenType() == COLON) {
      parseStmtLabel(b);
      return true;
    }
    if (b.getTokenType() == GOTO) {
      parseStmtGoto(b);
      return true;
    }
    return parseStmtSimple(b, true);
  }

  private void parseStmtIf(@NotNull PsiBuilder b) {
    assert b.getTokenType() == IF;
    PsiBuilder.Marker m = b.mark();
    parseStmtIfBranch(b);
    List<Pair<PsiBuilder.Marker, IonToken>> toClose = new ArrayList<>();
    while (match(b, ELSE)) {
      toClose.add(Pair.create(b.mark(), ELSE));
      b.advanceLexer();
      if (match(b, IF)) {
        toClose.add(Pair.create(b.mark(), IF));
        parseStmtIfBranch(b);
      } else {
        expectStmtBlock(b);
        break;
      }
    }
    for (Pair<PsiBuilder.Marker, IonToken> node : ContainerUtil.reverse(toClose)) {
      if (node.second == IF) {
        node.first.done(STMT_IF);
      } else {
        node.first.done(STMT_ELSE);
      }
    }
    m.done(STMT_IF);
  }

  private void parseStmtIfBranch(@NotNull PsiBuilder b) {
    assert b.getTokenType() == IF;
    b.advanceLexer();
    if (expect(b, LPAREN)) {
      if (parseStmtInit(b, false)) {
        if (consume(b, SEMICOLON)) {
          expectExpr(b);
        }
      } else {
        expectExpr(b);
      }
    }
    expect(b, RPAREN);
    expectStmtBlock(b);
  }

  private boolean parseStmtInit(@NotNull PsiBuilder b, boolean expectSemi) {
    if (match(b, NAME)) {
      if (lookAhead(b, 1, COLON_ASSIGN)) {
        PsiBuilder.Marker m = b.mark();
        b.advanceLexer();
        b.advanceLexer();
        expectExpr(b);
        if (expectSemi) {
          expect(b, SEMICOLON);
        }
        m.done(STMT_INIT);
        return true;
      }
      if (lookAhead(b, 1, COLON)) {
        PsiBuilder.Marker m = b.mark();
        b.advanceLexer();
        b.advanceLexer();
        expectType(b);
        if (consume(b, ASSIGN)) {
          if (!consume(b, UNDEF)) {
            expectExpr(b);
          }
        }
        if (expectSemi) {
          consumeUntil(b, SEMICOLON);
        }
        m.done(STMT_INIT);
        return true;
      }
    }
    return false;
  }

  private void parseStmtWhile(@NotNull PsiBuilder b) {
    assert b.getTokenType() == WHILE;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (expect(b, LPAREN)) {
      expectExpr(b);
      expect(b, RPAREN);
      expectStmtBlock(b);
    }
    m.done(STMT_WHILE);
  }

  private void parseStmtDo(@NotNull PsiBuilder b) {
    assert b.getTokenType() == DO;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expectStmtBlock(b);
    if (expect(b, WHILE)) {
      if (expect(b, LPAREN)) {
        expectExpr(b);
        expect(b, RPAREN);
      }
      consumeUntil(b, SEMICOLON);
    }
    m.done(STMT_DO);
  }

  private void parseStmtFor(@NotNull PsiBuilder b) {
    assert b.getTokenType() == FOR;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    // 'for' without parens? looks like parse.c supports them
    if (expect(b, LPAREN)) {
      parseStmtInit(b, false);
      expect(b, SEMICOLON);
      parseExpr(b);
      expect(b, SEMICOLON);
      parseStmtSimple(b, false);
      expect(b, RPAREN);
      expectStmtBlock(b);
    }
    m.done(STMT_FOR);
  }

  private void parseStmtSwitch(@NotNull PsiBuilder b) {
    assert b.getTokenType() == SWITCH;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, LPAREN);
    expectExpr(b);
    expect(b, RPAREN);
    if (expect(b, LBRACE)) {
      while (!b.eof() && !match(b, RBRACE)) {
        parseStmtSwitchCase(b);
      }
      expect(b, RBRACE);
    }
    m.done(STMT_SWITCH);
  }

  private void parseStmtSwitchCase(@NotNull PsiBuilder b) {
    if (consume(b, DEFAULT)) {
      expect(b, COLON);
    } else if (consume(b, CASE)) {
      parseStmtSwitchCasePattern(b);
      while (consume(b, COMMA)) {
        parseStmtSwitchCasePattern(b);
      }
      expect(b, COLON);
    } else {
      b.error("Expected 'case' or 'default', got " + b.getTokenText());
    }
    while (!b.eof() && !match(b, RBRACE) && !match(b, CASE) && !match(b, DEFAULT)) {
      if (!parseStmt(b)) {
        b.error("Expected statement, got " + b.getTokenText());
        b.advanceLexer();
      }
    }
  }

  private void parseStmtSwitchCasePattern(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (expectExpr(b)) {
      if (consume(b, ELLIPSIS)) {
        expectExpr(b);
      }
      m.done(STMT_SWITCH_PATTERN);
    } else {
      m.drop();
    }
  }

  private boolean parseStmtSimple(@NotNull PsiBuilder b, boolean expectSemi) {
    if (parseStmtInit(b, expectSemi)) {
      return true;
    }
    PsiBuilder.Marker m = b.mark();
    if (parseExpr(b)) {
      if (consume(b, ASSIGN_OP)) {
        expectExpr(b);
        if (expectSemi && !consume(b, SEMICOLON)) {
          b.error("Expected ';', got " + b.getTokenText());
        }
        m.done(STMT_ASSIGN);
      } else {
        if (expectSemi && !consume(b, SEMICOLON)) {
          b.error("Expected ';', got " + b.getTokenText());
        }
        m.done(STMT_EXPR);
      }
      return true;
    }
    m.drop();
    return false;
  }

  private void parseStmtBreak(@NotNull PsiBuilder b) {
    assert b.getTokenType() == BREAK;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, SEMICOLON);
    m.done(STMT_BREAK);
  }

  private void parseStmtContinue(@NotNull PsiBuilder b) {
    assert b.getTokenType() == CONTINUE;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, SEMICOLON);
    m.done(STMT_CONTINUE);
  }

  private void parseStmtReturn(@NotNull PsiBuilder b) {
    assert b.getTokenType() == RETURN;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (!match(b, SEMICOLON)) {
      expectExpr(b);
    }
    expect(b, SEMICOLON);
    m.done(STMT_RETURN);
  }

  private void parseStmtNote(@NotNull PsiBuilder b) {
    assert b.getTokenType() == POUND || b.getTokenType() == AT;
    boolean requiresSemi = b.getTokenType() == POUND;
    PsiBuilder.Marker m = b.mark();
    parseNote(b);
    if (requiresSemi) {
      expect(b, SEMICOLON);
    }
    m.done(STMT_NOTE);
  }

  private void parseStmtLabel(@NotNull PsiBuilder b) {
    assert b.getTokenType() == COLON;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, NAME);
    m.done(STMT_LABEL);
  }

  private void parseStmtGoto(@NotNull PsiBuilder b) {
    assert b.getTokenType() == GOTO;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    PsiBuilder.Marker nameMark = b.mark();
    expect(b, NAME);
    nameMark.done(LABEL_NAME);
    expect(b, SEMICOLON);
    m.done(STMT_GOTO);
  }

  private void parseNote(@NotNull PsiBuilder b) {
    assert b.getTokenType() == AT || b.getTokenType() == POUND;
    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    expect(b, NAME);
    if (consume(b, LPAREN)) {
      if (!match(b, RPAREN)) {
        do {
          if (!parseNoteParam(b)) {
            b.error("Expected note parameter, got " + b.getTokenText());
            break;
          }
        } while (consume(b, COMMA));
      }
      consumeUntil(b, RPAREN, TOP_LEVEL_TOKENS);
    }
    m.done(NOTE);
  }

  private boolean parseNoteParam(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (match(b, NAME) && lookAhead(b, 1, ASSIGN)) {
      b.advanceLexer();
      b.advanceLexer();
      expectExpr(b);
      m.done(NOTE_PARAM);
      return true;
    }
    if (parseExpr(b)) {
      m.done(NOTE_PARAM);
      return true;
    }
    m.rollbackTo();
    return false;
  }

  private boolean expectExpr(@NotNull PsiBuilder b) {
    if (!parseExpr(b)) {
      b.error("Expected expression, got " + b.getTokenText());
      return false;
    }
    return true;
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
          PsiBuilder.Marker arg = b.mark();
          expectExpr(b);
          arg.done(EXPR_CALL_ARG);
          while (consume(b, COMMA)) {
            arg = b.mark();
            expectExpr(b);
            arg.done(EXPR_CALL_ARG);
          }
        }
        expect(b, RPAREN);
        m.done(EXPR_CALL);
      } else if (consume(b, LBRACKET)) {
        if (!expectExpr(b)) {
          b.advanceLexer();
        }
        expect(b, RBRACKET);
        m.done(EXPR_INDEX);
      } else if (consume(b, DOT)) {
        if (match(b, NAME)) {
          PsiBuilder.Marker nameExprMark = b.mark();
          consume(b, NAME);
          nameExprMark.done(EXPR_NAME);
        } else {
          b.error("Exprected name, got " + b.getTokenText());
        }
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
    if (consume(b, STRING) || consume(b, MULTILINE_STRING)) {
      m.done(EXPR_LITERAL_STR);
      return true;
    }
    if (consume(b, CHAR)) {
      m.done(EXPR_LITERAL_CHAR);
      return true;
    }
    if (match(b, NAME)) {
      if (lookAhead(b, 1, LBRACE)) {
        PsiBuilder.Marker typeName = b.mark();
        consume(b, NAME);
        typeName.done(TYPE_NAME);
        parseExprCompound(b);
        m.done(EXPR_LITERAL_COMPOUND_TYPED);
        return true;
      } else {
        consume(b, NAME);
        m.done(EXPR_NAME);
        return true;
      }
    }
    if (consume(b, NEW)) {
      if (consume(b, LPAREN)) {
        expectExpr(b);
        expect(b, RPAREN);
      }
      if (consume(b, LBRACKET)) {
        expectExpr(b);
        expect(b, RBRACKET);
      }
      if (!consume(b, UNDEF)) {
        parseExpr(b);
      }
      m.done(EXPR_NEW);
      return true;
    }
    if (consume(b, SIZEOF)) {
      if (expect(b, LPAREN)) {
        if (consume(b, COLON)) {
          expectType(b);
        } else {
          expectExpr(b);
        }
        expect(b, RPAREN);
      }
      m.done(EXPR_CALL);
      return true;
    }
    if (consume(b, ALIGNOF)) {
      if (expect(b, LPAREN)) {
        if (consume(b, COLON)) {
          expectType(b);
        } else {
          expectExpr(b);
        }
        expect(b, RPAREN);
      }
      m.done(EXPR_CALL);
      return true;
    }
    if (consume(b, TYPEOF)) {
      if (consume(b, LPAREN)) {
        if (consume(b, COLON)) {
          expectType(b);
        } else {
          expectExpr(b);
        }
        expect(b, RPAREN);
      }
      m.done(EXPR_CALL);
      return true;
    }
    if (consume(b, OFFSETOF)) {
      if (expect(b, LPAREN)) {
        expectType(b);
        expect(b, COMMA);
        expect(b, NAME);
        expect(b, RPAREN);
      }
      m.done(EXPR_CALL);
      return true;
    }
    if (match(b, LBRACE)) {
      parseExprCompound(b);
      m.drop();
      return true;
    }
    if (consume(b, LPAREN)) {
      if (consume(b, COLON)) {
        parseType(b);
        expect(b, RPAREN);
        if (match(b, LBRACE)) {
          parseExprCompound(b);
        } else {
          parseExprUnary(b);
        }
        m.done(EXPR_CAST);
        return true;
      } else {
        parseExpr(b);
        expect(b, RPAREN);
        m.done(EXPR_PAREN);
        return true;
      }
    }
    m.drop();
    return false;
  }

  private boolean parseExprCompound(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (expect(b, LBRACE)) {
      if (!consume(b, RBRACE)) {
        parseExprCompoundField(b);
        while (consume(b, COMMA)) {
          parseExprCompoundField(b);
        }
        consumeUntil(b, RBRACE);
      }
      m.done(EXPR_LITERAL_COMPOUND);
      return true;
    } else {
      return false;
    }
  }

  private boolean parseExprCompoundField(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (consume(b, LBRACKET)) {
      parseExpr(b);
      expect(b, RBRACKET);
      expect(b, ASSIGN);
      parseExpr(b);
      m.done(COMPOUND_FIELD_INDEX);
      return true;
    } else {
      if (match(b, NAME) && lookAhead(b, 1, ASSIGN)) {
        PsiBuilder.Marker nameMark = b.mark();
        b.advanceLexer();
        nameMark.done(EXPR_NAME);
        b.advanceLexer();
        parseExpr(b);
        m.done(COMPOUND_FIELD_NAMED);
        return true;
      } else {
        parseExpr(b);
        m.done(COMPOUND_FIELD);
        return true;
      }
    }
  }

  private boolean expectType(@NotNull PsiBuilder b) {
    if (!parseType(b)) {
      b.error("Exprected type, got " + b.getTokenText());
      return false;
    }
    return true;
  }

  private boolean parseType(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (!parseTypeBase(b)) {
      m.drop();
      return false;
    }
    while (match(b, LBRACKET, CONST, MUL)) {
      if (consume(b, LBRACKET)) {
        PsiBuilder.Marker outer = m.precede();
        if (!match(b, RBRACKET)) {
          parseExpr(b);
        }
        boolean ok = expect(b, RBRACKET);
        m.done(TYPE_ARRAY);
        m = outer;
        if (!ok) {
          break;
        }
      } else if (consume(b, CONST)) {
        PsiBuilder.Marker outer = m.precede();
        m.done(TYPE_CONST);
        m = outer;
      } else if (consume(b, MUL)) {
        PsiBuilder.Marker outer = m.precede();
        m.done(TYPE_PTR);
        m = outer;
      }
    }
    m.drop();
    return true;
  }

  private boolean parseTypeBase(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (match(b, NAME)) {
      PsiBuilder.Marker name1 = b.mark();
      consume(b, NAME);
      name1.done(TYPE_NAME);
      boolean ok = true;
      while (ok && consume(b, DOT)) {
        if (match(b, NAME)) {
          PsiBuilder.Marker name2 = b.mark();
          consume(b, NAME);
          name2.done(TYPE_NAME);
        } else {
          b.error("Expected name, got " + b.getTokenText());
          ok = false;
        }
        m.done(TYPE_QNAME);
        m = m.precede();
      }
      m.drop();
      return true;
    }
    if (consume(b, FUNC)) {
      if (expect(b, LPAREN)) {
        if (!consume(b, RPAREN)) {
          if (parseTypeFuncParam(b)) {
            while (consume(b, COMMA)) {
              if (!parseTypeFuncParam(b)) {
                b.error("Expected parameter, got " + b.getTokenText());
                break;
              }
            }
          } else {
            b.error("Expected parameter, got " + b.getTokenText());
          }
          expect(b, RPAREN);
        }
        if (consume(b, COLON)) {
          if (!parseType(b)) {
            b.error("Expected type, got " + b.getTokenText());
          }
        }
      }
      m.done(TYPE_FUNC);
      return true;
    }
    if (consume(b, LPAREN)) {
      parseType(b);
      expect(b, RPAREN);
      m.done(TYPE_PAR);
      return true;
    }
    if (consume(b, LBRACE)) {
      if (parseType(b)) {
        while (consume(b, COMMA)) {
          if (!parseType(b)) {
            b.error("Expected type, got: " + b.getTokenText());
            break;
          }
        }
      } else {
        // either tuple without any field (valid) or a broken type if
        // the current symbol is not '}', show error in the latter case
        if (!match(b, RBRACE)) {
          b.error("Expected type, got: " + b.getTokenText());
        }
      }
      consumeUntil(b, RBRACE);
      m.done(TYPE_TUPLE);
      return true;
    }
    m.drop();
    return false;
  }

  private boolean parseTypeFuncParam(@NotNull PsiBuilder b) {
    PsiBuilder.Marker m = b.mark();
    if (consume(b, ELLIPSIS)) {
      m.done(TYPE_FUNC_PARAM);
      return true;
    }
    if (match(b, NAME) && lookAhead(b, 1, COLON)) {
      b.advanceLexer();
      b.advanceLexer();
      if (!parseType(b)) {
        b.error("Expected type, got: " + b.getTokenText());
      }
      m.done(TYPE_FUNC_PARAM);
      return true;
    } else {
      if (parseType(b)) {
        m.done(TYPE_FUNC_PARAM);
        return true;
      } else {
        b.error("Expected type, got: " + b.getTokenText());
        m.drop();
        return false;
      }
    }
  }

  private boolean expect(@NotNull PsiBuilder b, @NotNull IElementType token) {
    if (b.getTokenType() == token) {
      b.advanceLexer();
      return true;
    } else {
      String actual = b.eof() ? "eof" : b.getTokenText();
      b.error("Expected " + token.toString() + ", got " + actual);
      return false;
    }
  }

  private boolean consume(@NotNull PsiBuilder b, @NotNull IElementType token) {
    if (b.getTokenType() == token) {
      b.advanceLexer();
      return true;
    } else {
      return false;
    }
  }

  private boolean consume(@NotNull PsiBuilder b, @NotNull TokenSet tokens) {
    if (tokens.contains(b.getTokenType())) {
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

  private boolean match(@NotNull PsiBuilder b, @NotNull TokenSet tokens) {
    return tokens.contains(b.getTokenType());
  }

  private boolean lookAhead(@NotNull PsiBuilder b, int n, @NotNull IElementType... tokens) {
    IElementType actual = b.lookAhead(n);
    for (IElementType token : tokens) {
      if (token == actual) {
        return true;
      }
    }
    return false;
  }

  private void consumeUntil(@NotNull PsiBuilder b, @NotNull IElementType token) {
    if (!consume(b, token)) {
      b.error("Expected '" + token.toString() + "', got: " + b.getTokenText());
      while (!b.eof() && !consume(b, token)) {
        b.advanceLexer();
      }
    }
  }

  private void consumeUntil(@NotNull PsiBuilder b, @NotNull IElementType token, @NotNull TokenSet tokens) {
    if (!consume(b, token)) {
      b.error("Expected '" + token.toString() + "', got: " + b.getTokenText());
      while (!b.eof() && !consume(b, token) && !match(b, tokens)) {
        b.advanceLexer();
      }
    }
  }
}
