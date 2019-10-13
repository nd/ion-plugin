package ion;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import static ion.psi.IonElementType.IMPORT_DECL;
import static ion.psi.IonElementType.IMPORT_ITEM;
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
